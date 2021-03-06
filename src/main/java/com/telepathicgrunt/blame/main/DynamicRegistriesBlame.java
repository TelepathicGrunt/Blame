package com.telepathicgrunt.blame.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * I added check to make sure the message is only printed to logs if
 * DynamicRegistries is classloaded in non-vanilla places and added
 * info to the error message for those to read.
 */
public class DynamicRegistriesBlame {

    public static void classloadedCheck() {
        // After the main mod startup is done, the dynamic registry should be safe to classload. I believe.
        // So don't do any reports afterwards.
        if(Blame.MAIN_MOD_STARTUPS_FINISHED) return;

        Blame.LOGGER.log(Level.ERROR,
                "\n****************** Blame Report " + Blame.VERSION + " ******************" +
                        "\n\n Oh no! Oh god! Someone classloaded DynamicRegistries class way too early!" +
                        "\n Most registry entries for other mods is broken now! Please read the following stacktrace" +
                        "\n and see if you can find which mod broke the game badly and please show them this log file." +
                        "\n (If you can't tell which mod, let Blame creator, TelepathicGrunt, know!)" +
                        "\n\n If you are the modder notified, you may be registering/accessing the dynamic" +
                        "\n registries before the world is loaded/made. Please use the registries in" +
                        "\n WorldGenRegistries as that is what DynamicRegistries will copy from when " +
                        "\n vanilla creates/loads the world (and is why loading DynamicRegistries early breaks all mods)\n");
        Thread.dumpStack();
    }


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
    public static void printUnregisteredWorldgenConfiguredStuff(net.minecraft.util.registry.DynamicRegistries.Impl imp) {
        // Gets where this is firing. We want to not print info multiple times.
        // Specifically, clientside sync should be ignored as unregistered stuff
        // is more of a dedicated/integrated server issue.
        StackTraceElement stack = Thread.currentThread().getStackTrace()[4];
        if (stack.getClassName().equals("net.minecraft.client.network.play.ClientPlayNetHandler")) {
            return;
        }

        // Create a store here to minimize memory impact and let it get garbaged collected later.
        Map<String, Set<ResourceLocation>> unconfiguredStuffMap = new HashMap<>();
        HashSet<String> brokenConfiguredStuffSet = new HashSet<>();
        Set<String> collectedPossibleIssueMods = new HashSet<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Pattern pattern = Pattern.compile("\"(?:Name|type|location)\": *\"([a-z0-9_.-:]+)\"");

        // ConfiguredFeatures
        imp.registry(Registry.CONFIGURED_FEATURE_REGISTRY).ifPresent(configuredFeatureRegistry ->
                imp.registry(Registry.BIOME_REGISTRY).ifPresent(mutableRegistry -> mutableRegistry.entrySet()
                        .forEach(mapEntry -> findUnregisteredConfiguredFeatures(mapEntry, unconfiguredStuffMap, brokenConfiguredStuffSet, configuredFeatureRegistry, gson))));

        printUnregisteredStuff(unconfiguredStuffMap, "ConfiguredFeature");
        extractModNames(unconfiguredStuffMap, collectedPossibleIssueMods, pattern);

        // ConfiguredStructures
        imp.registry(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY).ifPresent(configuredStructureRegistry ->
                imp.registry(Registry.BIOME_REGISTRY).ifPresent(mutableRegistry -> mutableRegistry.entrySet()
                        .forEach(mapEntry -> findUnregisteredConfiguredStructures(mapEntry, unconfiguredStuffMap, configuredStructureRegistry, gson))));

        printUnregisteredStuff(unconfiguredStuffMap, "ConfiguredStructure");
        extractModNames(unconfiguredStuffMap, collectedPossibleIssueMods, pattern);

        // ConfiguredCarvers
        imp.registry(Registry.CONFIGURED_CARVER_REGISTRY).ifPresent(configuredCarverRegistry ->
                imp.registry(Registry.BIOME_REGISTRY).ifPresent(mutableRegistry -> mutableRegistry.entrySet()
                        .forEach(mapEntry -> findUnregisteredConfiguredCarver(mapEntry, unconfiguredStuffMap, configuredCarverRegistry, gson))));

        printUnregisteredStuff(unconfiguredStuffMap, "ConfiguredStructure");
        extractModNames(unconfiguredStuffMap, collectedPossibleIssueMods, pattern);

        if (collectedPossibleIssueMods.size() != 0) {
            // Add extra info to the log.
            String errorReport = "\n\n-----------------------------------------------------------------------" +
                    "\n****************** Blame Report " + Blame.VERSION + " ******************" +
                    "\n\n This is an experimental report. It is suppose to automatically read" +
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

    private static void extractModNames(Map<String, Set<ResourceLocation>> unconfiguredStuffMap, Set<String> collectedPossibleIssueMods, Pattern pattern) {
        unconfiguredStuffMap.keySet()
                .forEach(jsonString -> {
                    Matcher match = pattern.matcher(jsonString);
                    while (match.find()) {
                        if (!match.group(1).contains("minecraft:")) {
                            collectedPossibleIssueMods.add(match.group(1));
                        }
                    }
                });
        unconfiguredStuffMap.clear();
    }

    /**
     * Prints all unregistered configured features to the log.
     */
    private static void findUnregisteredConfiguredFeatures(
            Map.Entry<RegistryKey<Biome>, Biome> mapEntry,
            Map<String, Set<ResourceLocation>> unregisteredFeatureMap,
            HashSet<String> brokenConfiguredStuffSet,
            MutableRegistry<ConfiguredFeature<?, ?>> configuredFeatureRegistry,
            Gson gson) {

        for (List<Supplier<ConfiguredFeature<?, ?>>> generationStageList : mapEntry.getValue().getGenerationSettings().features()) {
            for (Supplier<ConfiguredFeature<?, ?>> configuredFeatureSupplier : generationStageList) {

                ResourceLocation biomeID = mapEntry.getKey().location();
                if (configuredFeatureRegistry.getKey(configuredFeatureSupplier.get()) == null &&
                        WorldGenRegistries.CONFIGURED_FEATURE.getKey(configuredFeatureSupplier.get()) == null) {
                    try {
                        ConfiguredFeature.CODEC
                                .encode(configuredFeatureSupplier, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left()
                                .ifPresent(configuredFeatureJSON ->
                                        cacheUnregisteredObject(
                                                configuredFeatureJSON,
                                                unregisteredFeatureMap,
                                                biomeID,
                                                gson));
                    }
                    catch (Throwable e) {
                        if (!brokenConfiguredStuffSet.contains(configuredFeatureSupplier.toString())) {
                            brokenConfiguredStuffSet.add(configuredFeatureSupplier.toString());

                            ConfiguredFeature<?, ?> cf = configuredFeatureSupplier.get();

                            // Getting bottommost cf way is from Quark. Very nice!
                            Feature<?> feature = cf.feature;
                            IFeatureConfig config = cf.config;

                            // Get the base feature of the CF. Will not get nested CFs such as trees in Feature.RANDOM_SELECTOR.
                            while (config instanceof DecoratedFeatureConfig) {
                                DecoratedFeatureConfig decoratedConfig = (DecoratedFeatureConfig) config;
                                feature = decoratedConfig.feature.get().feature;
                                config = decoratedConfig.feature.get().config;
                            }

                            String errorReport = "\n****************** Experimental Blame Report " + Blame.VERSION + " ******************" +
                                    "\n\n Found a ConfiguredFeature that was unabled to be turned into JSON which is... bad." +
                                    "\n This is all the info we can get about this strange... object." +
                                    "\n Top level cf [feature:" + configuredFeatureSupplier.toString() + " | config: " + configuredFeatureSupplier.get().toString() + "]" +
                                    "\n bottomost level cf [feature:" + feature.toString() + " | config: " + config.toString() + "]" +
                                    "\n\n";

                            // Log it to the latest.log file as well.
                            Blame.LOGGER.log(Level.ERROR, errorReport);
                        }
                    }
                }
            }
        }
    }

    /**
     * Prints all unregistered configured structures to the log.
     */
    private static void findUnregisteredConfiguredStructures(
            Map.Entry<RegistryKey<Biome>, Biome> mapEntry,
            Map<String, Set<ResourceLocation>> unregisteredStructureMap,
            MutableRegistry<StructureFeature<?, ?>> configuredStructureRegistry,
            Gson gson) {
        for (Supplier<StructureFeature<?, ?>> configuredStructureSupplier : mapEntry.getValue().getGenerationSettings().structures()) {

            ResourceLocation biomeID = mapEntry.getKey().location();
            if (configuredStructureRegistry.getKey(configuredStructureSupplier.get()) == null &&
                    WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE.getKey(configuredStructureSupplier.get()) == null) {
                StructureFeature.DIRECT_CODEC
                        .encode(configuredStructureSupplier.get(), JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left()
                        .ifPresent(configuredStructureJSON ->
                                cacheUnregisteredObject(
                                        configuredStructureJSON,
                                        unregisteredStructureMap,
                                        biomeID,
                                        gson));
            }
        }
    }

    /**
     * Prints all unregistered configured carver to the log.
     */
    private static void findUnregisteredConfiguredCarver(
            Map.Entry<RegistryKey<Biome>, Biome> mapEntry,
            Map<String, Set<ResourceLocation>> unregisteredCarverMap,
            MutableRegistry<ConfiguredCarver<?>> configuredCarverRegistry,
            Gson gson) {
        for (GenerationStage.Carving carvingStage : GenerationStage.Carving.values()) {
            for (Supplier<ConfiguredCarver<?>> configuredCarverSupplier : mapEntry.getValue().getGenerationSettings().getCarvers(carvingStage)) {

                ResourceLocation biomeID = mapEntry.getKey().location();
                if (configuredCarverRegistry.getKey(configuredCarverSupplier.get()) == null &&
                        WorldGenRegistries.CONFIGURED_CARVER.getKey(configuredCarverSupplier.get()) == null) {
                    ConfiguredCarver.DIRECT_CODEC
                            .encode(configuredCarverSupplier.get(), JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left()
                            .ifPresent(configuredCarverJSON ->
                                    cacheUnregisteredObject(
                                            configuredCarverJSON,
                                            unregisteredCarverMap,
                                            biomeID,
                                            gson));
                }
            }
        }
    }

    private static void cacheUnregisteredObject(
            JsonElement configuredObjectJSON,
            Map<String, Set<ResourceLocation>> unregisteredObjectMap,
            ResourceLocation biomeID,
            Gson gson) {
        String configuredObjectString = gson.toJson(configuredObjectJSON);

        if (!unregisteredObjectMap.containsKey(configuredObjectString))
            unregisteredObjectMap.put(configuredObjectString, new HashSet<>());

        unregisteredObjectMap.get(configuredObjectString).add(biomeID);
    }

    private static void printUnregisteredStuff(Map<String, Set<ResourceLocation>> unregisteredStuffMap, String type) {
        for (Map.Entry<String, Set<ResourceLocation>> entry : unregisteredStuffMap.entrySet()) {

            // Add extra info to the log.
            String errorReport = "\n****************** Blame Report " + Blame.VERSION + " ******************" +
                    "\n\n This " + type + " was found to be not registered. Look at the JSON info and try to" +
                    "\n find which mod it belongs to. Then go tell that mod owner to register their " + type +
                    "\n as otherwise, it will break other mods or datapacks that registered their stuff." +
                    "\n\n JSON info : " + entry.getKey() +
                    "\n\n Biome affected : " + entry.getValue().toString().replaceAll("(([\\w :]*,){7})", "$1\n                  ") + "\n\n";

            // Log it to the latest.log file as well.
            Blame.LOGGER.log(Level.ERROR, errorReport);
        }
    }
}
