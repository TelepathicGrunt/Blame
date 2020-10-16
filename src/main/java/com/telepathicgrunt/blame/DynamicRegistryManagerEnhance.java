package com.telepathicgrunt.blame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import org.apache.logging.log4j.Level;

import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DynamicRegistryManagerEnhance {


    public static void printUnregisteredWorldgenConfiguredStuff(DynamicRegistryManager.Impl imp){
        // Create a store here to minimize memory impact and let it get garbaged collected later.
        Map<String, Set<Identifier>> unconfigured_stuff_map = new HashMap<>();
        Set<String> collected_possible_issue_mods = new HashSet<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Pattern pattern = Pattern.compile("\"(?:Name|type|location)\": *\"([a-z_:]+)\"");

        // ConfiguredFeatures
        imp.getOptional(Registry.CONFIGURED_FEATURE_WORLDGEN).ifPresent(configuredFeatureRegistry ->
                imp.getOptional(Registry.BIOME_KEY).ifPresent(mutableRegistry -> mutableRegistry.getEntries()
                        .forEach(mapEntry -> findUnregisteredConfiguredFeatures(mapEntry, unconfigured_stuff_map, configuredFeatureRegistry, gson))));

        printUnregisteredStuff(unconfigured_stuff_map, "ConfiguredFeature");
        extractModNames(unconfigured_stuff_map, collected_possible_issue_mods, pattern);

        // ConfiguredStructures
        imp.getOptional(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN).ifPresent(configuredStructureRegistry ->
                imp.getOptional(Registry.BIOME_KEY).ifPresent(mutableRegistry -> mutableRegistry.getEntries()
                        .forEach(mapEntry -> findUnregisteredConfiguredStructures(mapEntry, unconfigured_stuff_map, configuredStructureRegistry, gson))));

        printUnregisteredStuff(unconfigured_stuff_map, "ConfiguredStructure");
        extractModNames(unconfigured_stuff_map, collected_possible_issue_mods, pattern);

        // ConfiguredCarvers
        imp.getOptional(Registry.CONFIGURED_CARVER_WORLDGEN).ifPresent(configuredCarverRegistry ->
                imp.getOptional(Registry.BIOME_KEY).ifPresent(mutableRegistry -> mutableRegistry.getEntries()
                        .forEach(mapEntry -> findUnregisteredConfiguredCarver(mapEntry, unconfigured_stuff_map, configuredCarverRegistry, gson))));

        printUnregisteredStuff(unconfigured_stuff_map, "ConfiguredStructure");
        extractModNames(unconfigured_stuff_map, collected_possible_issue_mods, pattern);

        if(collected_possible_issue_mods.size() != 0){
            // Add extra info to the log.
            String errorReport = "\n****************** Blame Report ******************" +
                    "\n\n This is an experimental report. It is suppose to automatically read" +
                    "\n the JSON of all the unregistered ConfiguredFeatures, ConfiguredStructures," +
                    "\n and ConfiguredCarvers. Then does its best to collect the terms that seem to" +
                    "\n state whose mod the unregistered stuff belongs to." +
                    "\n\n Possible mods responsible for unregistered stuff: \n" +
                    collected_possible_issue_mods.stream().sorted().collect(Collectors.joining("\n")) + "\n\n";

            // Log it to the latest.log file as well.
            Blame.LOGGER.log(Level.ERROR, errorReport);
        }
        collected_possible_issue_mods.clear();
    }

    private static void extractModNames(Map<String, Set<Identifier>> unconfigured_stuff_map, Set<String> collected_possible_issue_mods, Pattern pattern) {
        unconfigured_stuff_map.keySet()
                .forEach(jsonString -> {
                    Matcher match = pattern.matcher(jsonString);
                    while(match.find()) {
                        if(!match.group(1).contains("minecraft:")){
                            collected_possible_issue_mods.add(match.group(1));
                        }
                    }
                });
        unconfigured_stuff_map.clear();
    }


    /**
     * Prints all unregistered configured features to the log.
     */
    private static void findUnregisteredConfiguredFeatures(
            Map.Entry<RegistryKey<Biome>, Biome>  mapEntry,
            Map<String, Set<Identifier>> unregistered_feature_map,
            MutableRegistry<ConfiguredFeature<?,?>> configuredFeatureRegistry,
            Gson gson)
    {

        for(List<Supplier<ConfiguredFeature<?, ?>>> generationStageList : mapEntry.getValue().getGenerationSettings().getFeatures()){
            for(Supplier<ConfiguredFeature<?, ?>> configuredFeatureSupplier : generationStageList){

                Identifier biomeID = mapEntry.getKey().getValue();
                if(configuredFeatureRegistry.getId(configuredFeatureSupplier.get()) == null &&
                        BuiltinRegistries.CONFIGURED_FEATURE.getId(configuredFeatureSupplier.get()) == null)
                {
                    ConfiguredFeature.REGISTRY_CODEC
                            .encode(configuredFeatureSupplier, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left()
                            .ifPresent(configuredFeatureJSON ->
                                    cacheUnregisteredObject(
                                            configuredFeatureJSON,
                                            unregistered_feature_map,
                                            biomeID,
                                            gson));
                }
            }
        }
    }

    /**
     * Prints all unregistered configured structures to the log.
     */
    private static void findUnregisteredConfiguredStructures(
            Map.Entry<RegistryKey<Biome>, Biome>  mapEntry,
            Map<String, Set<Identifier>> unregistered_structure_map,
            MutableRegistry<ConfiguredStructureFeature<?,?>> configuredStructureRegistry,
            Gson gson)
    {
        for(Supplier<ConfiguredStructureFeature<?, ?>> configuredStructureSupplier : mapEntry.getValue().getGenerationSettings().getStructureFeatures()){

            Identifier biomeID = mapEntry.getKey().getValue();
            if(configuredStructureRegistry.getId(configuredStructureSupplier.get()) == null &&
                    BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureSupplier.get()) == null)
            {
                ConfiguredStructureFeature.CODEC
                        .encode(configuredStructureSupplier.get(), JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left()
                        .ifPresent(configuredStructureJSON ->
                                cacheUnregisteredObject(
                                        configuredStructureJSON,
                                        unregistered_structure_map,
                                        biomeID,
                                        gson));
            }
        }
    }

    /**
     * Prints all unregistered configured carver to the log.
     */
    private static void findUnregisteredConfiguredCarver(
            Map.Entry<RegistryKey<Biome>, Biome>  mapEntry,
            Map<String, Set<Identifier>> unregistered_carver_map,
            MutableRegistry<ConfiguredCarver<?>> configuredCarverRegistry,
            Gson gson)
    {
        for(GenerationStep.Carver carvingStage : GenerationStep.Carver.values()) {
            for (Supplier<ConfiguredCarver<?>> configuredCarverSupplier : mapEntry.getValue().getGenerationSettings().getCarversForStep(carvingStage)) {

                Identifier biomeID = mapEntry.getKey().getValue();
                if(configuredCarverRegistry.getId(configuredCarverSupplier.get()) == null &&
                        BuiltinRegistries.CONFIGURED_CARVER.getId(configuredCarverSupplier.get()) == null)
                {
                    ConfiguredCarver.REGISTRY_CODEC
                            .encode(configuredCarverSupplier, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left()
                            .ifPresent(configuredCarverJSON ->
                                    cacheUnregisteredObject(
                                            configuredCarverJSON,
                                            unregistered_carver_map,
                                            biomeID,
                                            gson));
                }
            }
        }
    }

    private static void cacheUnregisteredObject(
            JsonElement configuredObjectJSON,
            Map<String, Set<Identifier>> unregistered_object_map,
            Identifier biomeID,
            Gson gson)
    {
        String cfstring = gson.toJson(configuredObjectJSON);

        if(!unregistered_object_map.containsKey(cfstring))
            unregistered_object_map.put(cfstring, new HashSet<>());

        unregistered_object_map.get(cfstring).add(biomeID);
    }

    private static void printUnregisteredStuff(Map<String, Set<Identifier>> UNREGISTERED_STUFF_MAP, String type){
        for(Map.Entry<String, Set<Identifier>> entry : UNREGISTERED_STUFF_MAP.entrySet()){

            // Add extra info to the log.
            String errorReport = "\n****************** Blame Report ******************" +
                    "\n\n This " + type + " was found to be not registered. Look at the JSON info and try to" +
                    "\n find which mod it belongs to. Then go tell that mod owner to register their " + type +
                    "\n as otherwise, it will break other mods or datapacks that registered their stuff." +
                    "\n\n JSON info : " + entry.getKey() +
                    "\n\n Biome affected : " + entry.getValue().toString().replaceAll("(([\\w :]*,){9})", "$1\n                  ") + "\n\n";

            // Log it to the latest.log file as well.
            Blame.LOGGER.log(Level.ERROR, errorReport);
        }
    }
}
