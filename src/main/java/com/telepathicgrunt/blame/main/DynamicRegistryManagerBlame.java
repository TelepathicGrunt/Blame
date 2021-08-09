package com.telepathicgrunt.blame.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DynamicRegistryManagerBlame {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Pattern PATTERN = Pattern.compile("\"(?:Name|type|location)\": *\"([a-z0-9_.-:]+)\"");

    /**
     * The main hook for the parser to work from. This will check every biomes in the
     * DynamicRegistry to see if it has exploded due to unregistered stuff added to it.
     * <p>
     * It compares to the string representation of all registered objects because if we
     * do a simple registry.get(), there seems to be a weird issue where the object in
     * the biome is not actually the same object as in the registry. Like the biome did
     * its own deep copy of what's in the registry as so, .get fails despite the object
     * actually being registered already. That's why we compare the stringified JSON
     * results instead.
     */
    public static void printUnregisteredWorldgenConfiguredStuff(DynamicRegistryManager dynamicRegistries) {
        // Small cache so we can skip already safe worldgen elements to speedup validating.
        Set<Object> checkedSafeElements = new HashSet<>();

        // Create a store here to minimize memory impact and let it get garbaged collected later.
        Map<String, Set<Identifier>> unconfiguredStuffMap = new HashMap<>();
        Map<String, String> brokenConfiguredWorldgenMap = new HashMap<>();
        Set<String> collectedPossibleIssueMods = new HashSet<>();

        // Grab all registries we will need.
        Registry<Biome> biomeRegistry = dynamicRegistries.getMutable(Registry.BIOME_KEY);
        Registry<ConfiguredFeature<?, ?>> configuredFeatureRegistry = dynamicRegistries.getMutable(Registry.CONFIGURED_FEATURE_KEY);
        Registry<ConfiguredCarver<?>> configuredCarverRegistry = dynamicRegistries.getMutable(Registry.CONFIGURED_CARVER_KEY);
        Registry<ConfiguredStructureFeature<?, ?>> configuredStructureRegistry = dynamicRegistries.getMutable(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY);

        int numberOfBiomes = biomeRegistry.getIds().size();

        // Checks every biome for unregistered or broken worldgen elements.
        for (Biome biome : biomeRegistry) {
            GenerationSettings biomeGenerationSettings = biome.getGenerationSettings();
            Identifier biomeName = biomeRegistry.getId(biome);

            biomeGenerationSettings.getFeatures().forEach(generationStageFeatureList ->
                    generationStageFeatureList.forEach(configuredFeatureSupplier -> {
                        ConfiguredFeature<?, ?> feature = configuredFeatureSupplier.get();
                        if (!checkedSafeElements.contains(feature)) {
                            boolean isValid = validate(feature,
                                    configuredFeatureRegistry,
                                    BuiltinRegistries.CONFIGURED_FEATURE,
                                    ConfiguredFeature.CODEC,
                                    biomeName,
                                    unconfiguredStuffMap,
                                    brokenConfiguredWorldgenMap);

                            if(isValid) checkedSafeElements.add(feature);
                        }
                    })
            );
        }

        printUnregisteredStuff(unconfiguredStuffMap, brokenConfiguredWorldgenMap, "ConfiguredFeature", numberOfBiomes);
        extractModNames(unconfiguredStuffMap, collectedPossibleIssueMods);

        for (Biome biome : biomeRegistry) {
            GenerationSettings biomeGenerationSettings = biome.getGenerationSettings();
            Identifier biomeName = biomeRegistry.getId(biome);

            for (GenerationStep.Carver carvingStage : GenerationStep.Carver.values()) {
                biomeGenerationSettings.getCarversForStep(carvingStage).forEach(configuredWorldCarverSupplier -> {
                    ConfiguredCarver<?> carver = configuredWorldCarverSupplier.get();
                    if (!checkedSafeElements.contains(carver)) {
                        boolean isValid = validate(carver,
                                configuredCarverRegistry,
                                BuiltinRegistries.CONFIGURED_CARVER,
                                ConfiguredCarver.CODEC,
                                biomeName,
                                unconfiguredStuffMap,
                                brokenConfiguredWorldgenMap);

                        if(isValid) checkedSafeElements.add(carver);
                    }
                });
            }
        }

        printUnregisteredStuff(unconfiguredStuffMap, brokenConfiguredWorldgenMap, "ConfiguredStructure", numberOfBiomes);
        extractModNames(unconfiguredStuffMap, collectedPossibleIssueMods);

        for (Biome biome : biomeRegistry) {
            GenerationSettings biomeGenerationSettings = biome.getGenerationSettings();
            Identifier biomeName = biomeRegistry.getId(biome);

            biomeGenerationSettings.getStructureFeatures().forEach(structureFeatureSupplier -> {
                ConfiguredStructureFeature<?, ?> structureFeature = structureFeatureSupplier.get();
                if (!checkedSafeElements.contains(structureFeature)) {
                    boolean isValid = validate(structureFeature,
                            configuredStructureRegistry,
                            BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE,
                            ConfiguredStructureFeature.CODEC,
                            biomeName,
                            unconfiguredStuffMap,
                            brokenConfiguredWorldgenMap);

                    if(isValid) checkedSafeElements.add(structureFeature);
                }
            });
        }

        printUnregisteredStuff(unconfiguredStuffMap, brokenConfiguredWorldgenMap, "ConfiguredStructure", numberOfBiomes);
        extractModNames(unconfiguredStuffMap, collectedPossibleIssueMods);

        if (collectedPossibleIssueMods.size() != 0) {
            // Add extra info to the log.
            String errorReport = "\n\n-----------------------------------------------------------------------" +
                    "\n****************** Blame Report " + Blame.VERSION + " ******************" +
                    "\n\n This is an lengthy report. It is suppose to automatically read" +
                    "\n the JSON of all the unregistered ConfiguredFeatures, ConfiguredStructures," +
                    "\n and ConfiguredCarvers. Then does its best to collect the terms that seem to" +
                    "\n state whose mod the unregistered stuff belongs to." +
                    "\n\nPossible mods responsible for unregistered stuff:\n\n" +
                    collectedPossibleIssueMods.stream().sorted().collect(Collectors.joining("\n")) +
                    "\n\n-----------------------------------------------------------------------\n\n";

            // Log it to the latest.log file as well.
            Blame.LOGGER.log(Level.ERROR, errorReport);
        }
        collectedPossibleIssueMods.clear();
    }


    /**
     * Handles checking if the worldgen element is broken (unable to be turned into JSON) or unregistered.
     * If an issue is found, this will pause the IDE so the modder can come here and see the error and the worldgenElement for which element is the issue.
     * The console output will have the Forge message as last thing when paused too.
     */
    private static <T> boolean validate(T worldgenElement, Registry<T> dynamicRegistry, Registry<T> preWorldRegistry, Codec<T> codec, Identifier biomeName, Map<String, Set<Identifier>> unconfiguredStuffMap, Map<String, String> brokenConfiguredStuffSet) {

        // Checks to make sure the element can be turned into JSON safely or else Minecraft will explode with vague errors.
        Either<JsonElement, DataResult.PartialResult<JsonElement>> parsedData = codec.encode(worldgenElement, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get();

        if(parsedData.right().isPresent()) {
            String partialResult = parsedData.right().get().toString().replace("DynamicException["+parsedData.right().get().message()+" ", "");
            partialResult = partialResult.substring(0, partialResult.length() - 1);

            Identifier registeredName = preWorldRegistry.getId(worldgenElement);
            if(registeredName == null) {
                registeredName = dynamicRegistry.getId(worldgenElement);
            }

            if(!partialResult.isEmpty() && brokenConfiguredStuffSet.containsKey(partialResult)){
                brokenConfiguredStuffSet.merge(partialResult, biomeName.toString(), (oldValue, newValue) -> oldValue + ", " + newValue);
                return false;
            }

            if(worldgenElement instanceof ConfiguredFeature<?, ?> cf){

                // Getting bottommost cf way is from Quark. Very nice!
                Feature<?> feature = cf.feature;
                FeatureConfig config = cf.config;

                // Get the base feature of the CF. Will not get nested CFs such as trees in Feature.RANDOM_SELECTOR.
                while (config instanceof DecoratedFeatureConfig decoratedConfig) {
                    feature = decoratedConfig.feature.get().feature;
                    config = decoratedConfig.feature.get().config;
                }

                String errorReport =
                        "\n Error msg is: " + parsedData.right().get().message() +
                                "\n Registry Name: " + registeredName +
                                "\n Top level cf [feature:" + cf.feature.toString() + " | config: " + cf.config.toString() + "]" +
                                "\n bottomost level cf [feature:" + feature.toString() + " | config: " + config.toString() + "]" +
                                "\n Partial JSON Result: " + partialResult +
                                "\n Biomes Affected: " + biomeName;

                brokenConfiguredStuffSet.put(partialResult, errorReport);
            }
            else {
                String errorReport =
                        "\n Error msg is: " + parsedData.right().get().message() +
                                "\n Registry Name: " + registeredName + "  | Instance: " + worldgenElement +
                                "\n Partial JSON Result: " + partialResult +
                                "\n Biomes Affected: " + biomeName;

                brokenConfiguredStuffSet.put(partialResult, errorReport);
            }

            return false;
        }
        else if (dynamicRegistry.getId(worldgenElement) == null && preWorldRegistry.getId(worldgenElement) == null) {
            parsedData.left().ifPresent(configuredFeatureSupplier2 -> cacheUnregisteredObject(configuredFeatureSupplier2, unconfiguredStuffMap, biomeName));
            return false;
        }

        return true;
    }

    private static void cacheUnregisteredObject(
            JsonElement configuredObjectJSON,
            Map<String, Set<Identifier>> unregisteredObjectMap,
            Identifier biomeID) {

        String configuredObjectString = GSON.toJson(configuredObjectJSON);

        if (!unregisteredObjectMap.containsKey(configuredObjectString))
            unregisteredObjectMap.put(configuredObjectString, new HashSet<>());

        unregisteredObjectMap.get(configuredObjectString).add(biomeID);
    }

    private static void printUnregisteredStuff(Map<String, Set<Identifier>> unregisteredStuffMap, Map<String, String> brokenConfiguredStuffSet, String type, int numberOfBiomes) {

        boolean printedInitialInfo = false;
        String initialInfo =
                "\n****************** Blame Report Broken Worldgen " + Blame.VERSION + " ******************" +
                        "\n\n These " + type + " were unabled to be turned into JSON which is... bad." +
                        "\n This is all the info we can get about the " + type + "\n";

        for (Map.Entry<String, String> entry : brokenConfiguredStuffSet.entrySet()) {
            Blame.LOGGER.log(Level.ERROR, (printedInitialInfo ? "" : initialInfo) + entry.getValue() + "\n\n");
            printedInitialInfo = true;
        }
        brokenConfiguredStuffSet.clear();


        printedInitialInfo = false;
        initialInfo =
                "\n****************** Blame Report Unregistered Worldgen " + Blame.VERSION + " ******************" +
                        "\n\n These " + type + " was found to be not registered. Look at the JSON info and try to" +
                        "\n find which mod it belongs to. Then go tell that mod owner to register their " + type +
                        "\n as otherwise, it will break other mods or datapacks that registered their stuff.\n";
        for (Map.Entry<String, Set<Identifier>> entry : unregisteredStuffMap.entrySet()) {

            String biomeAffected = numberOfBiomes == entry.getValue().size() ? "All biomes" : entry.getValue().toString().replaceAll("(([\\w :]*,){7})", "$1\n                  ");

            // Add extra info to the log.
            String errorReport = (printedInitialInfo ? "----------------------------------" : initialInfo) +
                    "\n Unregistered " + type + " JSON info : " + entry.getKey() +
                    "\n\n Biome affected : " + biomeAffected + "\n\n";

            // Log it to the latest.log file as well.
            Blame.LOGGER.log(Level.ERROR, errorReport);
            printedInitialInfo = true;
        }
    }

    private static void extractModNames(Map<String, Set<Identifier>> unconfiguredStuffMap, Set<String> collectedPossibleIssueMods) {
        unconfiguredStuffMap.keySet()
                .forEach(jsonString -> {
                    Matcher match = PATTERN.matcher(jsonString);
                    while (match.find()) {
                        if (!match.group(1).contains("minecraft:")) {
                            collectedPossibleIssueMods.add(match.group(1));
                        }
                    }
                });
        unconfiguredStuffMap.clear();
    }
}
