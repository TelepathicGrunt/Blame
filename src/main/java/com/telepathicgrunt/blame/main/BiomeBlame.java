package com.telepathicgrunt.blame.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;

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
    public static void addFeatureDetails(Biome biome, WorldGenRegion worldGenRegion, ConfiguredFeature<?, ?> configuredFeature) {
        DynamicRegistries dynamicRegistries = worldGenRegion.registryAccess();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String report;

        ResourceLocation configuredFeatureID = dynamicRegistries.registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY).getKey(configuredFeature);
        if (configuredFeatureID == null) configuredFeatureID = WorldGenRegistries.CONFIGURED_FEATURE.getKey(configuredFeature);

        ResourceLocation biomeID = dynamicRegistries.registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome);
        Either<JsonElement, DataResult.PartialResult<JsonElement>> configuredFeatureJSON = ConfiguredFeature.CODEC.encode(() -> configuredFeature, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).promotePartial(s -> {}).get();

        // Add extra info to the crash report file.
        if (configuredFeatureID == null) {
            report = "\n****************** Blame Report ConfiguredFeature " + Blame.VERSION + " ******************" +
                    "\n\n ConfiguredFeature name was unable to be found due to the configuredfeature not being registered." +
                    "\n Sorry but Blame isn't really able to get much info but..." +
                    "\n Here's the best attempt at turning the ConfiguredFeature to JSON for analysis: \n" + gson.toJson(configuredFeatureJSON.left().get());
        }
        else {
            report = "\n****************** Blame Report ConfiguredFeature " + Blame.VERSION + " ******************" +
                    "\n\n ConfiguredFeature Registry Name : " + configuredFeatureID +
                    "\n Biome Registry Name : " + (biomeID != null ? biomeID : "Wait what? How is the biome not registered and has no registry name!?!? This should be impossible!!!") +
                    "\n\n JSON info : " + (configuredFeatureJSON.left().isPresent() ? gson.toJson(configuredFeatureJSON.left().get()) : "Failed to get JSON somehow. Error:\n" + configuredFeatureJSON.right().toString()) + "\n\n";
        }

        // Log it to the latest.log file as well.
        Blame.LOGGER.log(Level.ERROR, report);
    }


    /**
     * Place blame on broke structures during worldgen.
     * Prints registry name of feature and biome.
     * Prints the crashlog to latest.log as well.
     */
    public static void addStructureDetails(Biome biome, WorldGenRegion worldGenRegion, Structure<?> structureFeature, CrashReport crashreport) {
        DynamicRegistries dynamicRegistries = worldGenRegion.registryAccess();

        ResourceLocation structureID = ForgeRegistries.STRUCTURE_FEATURES.getKey(structureFeature);
        ResourceLocation biomeID = dynamicRegistries.registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome);

        // Add extra info to the crash report file.
        // Note, only structures can do the details part as configuredfeatures always says the ConfiguredFeature class.
        crashreport.getSystemDetails()
                .setDetail("\n****************** Blame Report ConfiguredStructure " + Blame.VERSION + " ******************",
                        "\n\n Structure Name : " + structureFeature.getFeatureName() + // Never null
                                "\n Structure Registry Name : " + (structureID != null ? structureID : "Structure is not registered somehow. Yell at the mod author when found to register their structures!") +
                                "\n Structure Details : " + structureFeature +
                                "\n Biome Registry Name : " + (biomeID != null ? biomeID : "Wait what? How is the biome not registered and has no registry name!?!? This should be impossible!!!"));

        // Log it to the latest.log file as well.
        Blame.LOGGER.log(Level.ERROR, crashreport.getFriendlyReport());
    }
}
