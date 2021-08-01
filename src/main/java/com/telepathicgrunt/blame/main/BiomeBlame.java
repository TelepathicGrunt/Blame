package com.telepathicgrunt.blame.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.Optional;

/* @author - TelepathicGrunt
 *
 * Two small mixins to make crashes during feature gen and structure gen now
 * output info about the feature, structure, and biome into the crashlog and
 * into the latest.log. Basically it needs more info as it is impossible
 * to find the broken feature before.
 *
 * LGPLv3
 */
public class BiomeBlame {
    /**
     * Place blame on broke feature during worldgen.
     * Prints registry name of feature and biome.
     * Prints the crashlog to latest.log as well.
     */
    public static void addFeatureDetails(Biome biome, ChunkRegion chunkRegion, ConfiguredFeature<?, ?> configuredFeature) {
        DynamicRegistryManager dynamicRegistryManager = chunkRegion.getRegistryManager();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Identifier configuredFeatureID = dynamicRegistryManager.getMutable(Registry.CONFIGURED_FEATURE_KEY).getId(configuredFeature);
        if (configuredFeatureID == null) configuredFeatureID = BuiltinRegistries.CONFIGURED_FEATURE.getId(configuredFeature);

        Identifier biomeID = dynamicRegistryManager.getMutable(Registry.BIOME_KEY).getId(biome);
        Either<JsonElement, DataResult.PartialResult<JsonElement>> configuredFeatureJSON = ConfiguredFeature.REGISTRY_CODEC.encode(() -> configuredFeature, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).promotePartial(s -> {}).get();

        // Add extra info to the latest.log file.
        if (configuredFeatureID == null) {
            Blame.LOGGER.error("\n****************** Blame Report ConfiguredFeature " + Blame.VERSION + " ******************" +
                            "\n\n ConfiguredFeature name was unable to be found due to either the configuredfeature registry or " +
                            "\n biome registry missing somehow. Or that the configuredfeature is not in any registries." +
                            "\n Sorry but Blame isn't really able to get much info but..." +
                            "\n Here's the best attempt at turning the configuredfeature to JSON for analysis: \n" + gson.toJson(configuredFeatureJSON.left().get()));
        }
        else {
            Blame.LOGGER.error("\n****************** Blame Report ConfiguredFeature " + Blame.VERSION + " ******************" +
                            "\n\n ConfiguredFeature Registry Name : " + configuredFeatureID +
                            "\n Biome Registry Name : " + (biomeID != null ? biomeID : "Wait what? How is the biome not registered and has no registry name!?!? This should be impossible!!!") +
                            "\n\n JSON info : " + (configuredFeatureJSON.left().isPresent() ? gson.toJson(configuredFeatureJSON.left().get()) : "Failed to get JSON somehow. Error:\n" + configuredFeatureJSON.right().toString()) + "\n\n");
        }
    }


    /**
     * Place blame on broke structures during worldgen.
     * Prints registry name of structure and biome.
     * Prints the crashlog to latest.log as well.
     */
    public static void addStructureDetails(Biome biome, ChunkRegion chunkRegion, StructureFeature<?> structureFeature) {
        DynamicRegistryManager dynamicRegistryManager = chunkRegion.getRegistryManager();

        Identifier structureID = null;
        Identifier biomeID = null;

        try {
            structureID = Registry.STRUCTURE_FEATURE.getId(structureFeature);
            biomeID = dynamicRegistryManager.get(Registry.BIOME_KEY).getId(biome);
        }
        catch (Throwable ignored) {
        }

        // Add extra info to the latest.log file.
        // Note, only structures can do the details part as configuredfeatures always says the ConfiguredFeature class.
        Blame.LOGGER.error("\n****************** Blame Report ConfiguredStructure " + Blame.VERSION + " ******************" +
                        "\n\n Structure Name : " + structureFeature.getName() + // Never null
                        "\n Structure Registry Name : " + (structureID != null ? structureID : "Structure is not registered somehow. Yell at the mod author when found to register their structures!") +
                        "\n Structure Details : " + structureFeature +
                        "\n Biome Registry Name : " + (biomeID != null ? biomeID : "Wait what? How is the biome not registered and has no registry name!?!? This should be impossible!!!"));
    }
}
