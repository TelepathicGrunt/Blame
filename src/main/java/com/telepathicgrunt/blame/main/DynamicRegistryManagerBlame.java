package com.telepathicgrunt.blame.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DynamicRegistryManagerBlame {

    public static void printUnregisteredWorldgenConfiguredStuff(DynamicRegistryManager.Impl imp) {

        // Create a store here to minimize memory impact and let it get garbaged collected later.
        Map<String, Set<Identifier>> unconfiguredStuffMap = new HashMap<>();
        Set<String> collectedPossibleIssueMods = new HashSet<>();
        HashSet<String> brokenConfiguredStuffSet = new HashSet<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Pattern pattern = Pattern.compile("\"(?:Name|type|location)\": *\"([a-z0-9_.-:]+)\"");

        // ConfiguredFeatures
        imp.getOptional(Registry.CONFIGURED_FEATURE_KEY).ifPresent(configuredFeatureRegistry ->
                imp.getOptional(Registry.BIOME_KEY).ifPresent(mutableRegistry -> mutableRegistry.getEntries()
                        .forEach(mapEntry -> findUnregisteredConfiguredFeatures(mapEntry, unconfiguredStuffMap, brokenConfiguredStuffSet, configuredFeatureRegistry, gson))));

        printUnregisteredStuff(unconfiguredStuffMap, "ConfiguredFeature");
        extractModNames(unconfiguredStuffMap, collectedPossibleIssueMods, pattern);

        // ConfiguredStructures
        imp.getOptional(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY).ifPresent(configuredStructureRegistry ->
                imp.getOptional(Registry.BIOME_KEY).ifPresent(mutableRegistry -> mutableRegistry.getEntries()
                        .forEach(mapEntry -> findUnregisteredConfiguredStructures(mapEntry, unconfiguredStuffMap, configuredStructureRegistry, gson))));

        printUnregisteredStuff(unconfiguredStuffMap, "ConfiguredStructure");
        extractModNames(unconfiguredStuffMap, collectedPossibleIssueMods, pattern);

        // ConfiguredCarvers
        imp.getOptional(Registry.CONFIGURED_CARVER_KEY).ifPresent(configuredCarverRegistry ->
                imp.getOptional(Registry.BIOME_KEY).ifPresent(mutableRegistry -> mutableRegistry.getEntries()
                        .forEach(mapEntry -> findUnregisteredConfiguredCarver(mapEntry, unconfiguredStuffMap, configuredCarverRegistry, gson))));

        printUnregisteredStuff(unconfiguredStuffMap, "ConfiguredStructure");
        extractModNames(unconfiguredStuffMap, collectedPossibleIssueMods, pattern);

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

    private static void extractModNames(Map<String, Set<Identifier>> unconfiguredStuffMap, Set<String> collectedPossibleIssueMods, Pattern pattern) {
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
            Map<String, Set<Identifier>> unregisteredFeatureMap,
            HashSet<String> brokenConfiguredStuffSet,
            Registry<ConfiguredFeature<?, ?>> configuredFeatureRegistry,
            Gson gson) {

        for (List<Supplier<ConfiguredFeature<?, ?>>> generationStageList : mapEntry.getValue().getGenerationSettings().getFeatures()) {
            for (Supplier<ConfiguredFeature<?, ?>> configuredFeatureSupplier : generationStageList) {

                Identifier biomeID = mapEntry.getKey().getValue();
                if (configuredFeatureRegistry.getId(configuredFeatureSupplier.get()) == null &&
                        BuiltinRegistries.CONFIGURED_FEATURE.getId(configuredFeatureSupplier.get()) == null) {
                    try {
                        ConfiguredFeature.REGISTRY_CODEC
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
                            FeatureConfig config = cf.config;

                            // Get the base feature of the CF. Will not get nested CFs such as trees in Feature.RANDOM_SELECTOR.
                            while (config instanceof DecoratedFeatureConfig) {
                                DecoratedFeatureConfig decoratedConfig = (DecoratedFeatureConfig) config;
                                feature = decoratedConfig.feature.get().feature;
                                config = decoratedConfig.feature.get().config;
                            }

                            String errorReport = "\n****************** Blame Report ConfiguredFeature JSON parse " + Blame.VERSION + " ******************" +
                                    "\n\n Found a ConfiguredFeature that was unabled to be turned into JSON which is... bad." +
                                    "\n This is all the info we can get about this strange... object." +
                                    "\n Top level cf [feature:" + configuredFeatureSupplier + " | config: " + configuredFeatureSupplier.get().toString() + "]" +
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
            Map<String, Set<Identifier>> unregisteredStructureMap,
            Registry<ConfiguredStructureFeature<?, ?>> configuredStructureRegistry,
            Gson gson) {
        for (Supplier<ConfiguredStructureFeature<?, ?>> configuredStructureSupplier : mapEntry.getValue().getGenerationSettings().getStructureFeatures()) {

            Identifier biomeID = mapEntry.getKey().getValue();
            if (configuredStructureRegistry.getId(configuredStructureSupplier.get()) == null &&
                    BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureSupplier.get()) == null) {
                ConfiguredStructureFeature.CODEC
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
            Map<String, Set<Identifier>> unregisteredCarverMap,
            Registry<ConfiguredCarver<?>> configuredCarverRegistry,
            Gson gson) {
        for (GenerationStep.Carver carvingStage : GenerationStep.Carver.values()) {
            for (Supplier<ConfiguredCarver<?>> configuredCarverSupplier : mapEntry.getValue().getGenerationSettings().getCarversForStep(carvingStage)) {

                Identifier biomeID = mapEntry.getKey().getValue();
                if (configuredCarverRegistry.getId(configuredCarverSupplier.get()) == null &&
                        BuiltinRegistries.CONFIGURED_CARVER.getId(configuredCarverSupplier.get()) == null) {
                    ConfiguredCarver.REGISTRY_CODEC
                            .encode(configuredCarverSupplier, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left()
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
            Map<String, Set<Identifier>> unregisteredObjectMap,
            Identifier biomeID,
            Gson gson) {
        String cfstring = gson.toJson(configuredObjectJSON);

        if (!unregisteredObjectMap.containsKey(cfstring))
            unregisteredObjectMap.put(cfstring, new HashSet<>());

        unregisteredObjectMap.get(cfstring).add(biomeID);
    }

    private static void printUnregisteredStuff(Map<String, Set<Identifier>> unregisteredStuffMap, String type) {
        for (Map.Entry<String, Set<Identifier>> entry : unregisteredStuffMap.entrySet()) {

            // Add extra info to the log.
            String errorReport = "\n****************** Blame Report Unregistered Worldgen " + Blame.VERSION + " ******************" +
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
