package com.telepathicgrunt.blame.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
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
    public static void addFeatureDetails(Biome biome, WorldGenRegion worldGenRegion, ConfiguredFeature<?, ?> configuredFeature, CrashReport crashreport) {
        DynamicRegistries dynamicRegistries = worldGenRegion.registryAccess();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ResourceLocation configuredFeatureID = null;
        ResourceLocation biomeID = null;
        Optional<JsonElement> configuredFeatureJSON = Optional.empty();
        StackTraceElement[] trace = null;

        try {
            configuredFeatureID = dynamicRegistries.registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY).getKey(configuredFeature);
            if (configuredFeatureID == null) {
                configuredFeatureID = WorldGenRegistries.CONFIGURED_FEATURE.getKey(configuredFeature);
            }
            biomeID = dynamicRegistries.registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome);
        }
        catch (Throwable ignored) {
        }

        try {
            configuredFeatureJSON = ConfiguredFeature.CODEC.encode(() -> configuredFeature, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left();
        }
        catch (Throwable e) {
            trace = e.getStackTrace();
        }

        // Add extra info to the crash report file.
        if (configuredFeatureID == null) {
            crashreport.getSystemDetails()
                    .setDetail("\n****************** Blame Report " + Blame.VERSION + " ******************",
                            "\n\n ConfiguredFeature name was unable to be found due to either the configuredfeature registry or " +
                                    "\n biome registry missing somehow. Or that the configuredfeature is not in any registries." +
                                    "\n Sorry but Blame isn't really able to get much info but..." +
                                    "\n Here's the best attempt at turning the configuredfeature to JSON for analysis: \n" + (configuredFeatureJSON.isPresent() ? gson.toJson(configuredFeatureJSON.get()) : ""));
        }
        else {
            crashreport.getSystemDetails()
                    .setDetail("\n****************** Blame Report " + Blame.VERSION + " ******************",
                            "\n\n ConfiguredFeature Registry Name : " + configuredFeatureID +
                                    "\n Biome Registry Name : " + (biomeID != null ? biomeID : "Wait what? How is the biome not registered and has no registry name!?!? This should be impossible!!!") +
                                    "\n\n JSON info : " + (configuredFeatureJSON.isPresent() ? gson.toJson(configuredFeatureJSON.get()) : "Failed to get JSON somehow. Stacktrace of error:\n" + Arrays.toString(trace)) + "\n\n");
        }

        // Log it to the latest.log file as well.
        Blame.LOGGER.log(Level.ERROR, crashreport.getFriendlyReport());
    }


    /**
     * Place blame on broke structures during worldgen.
     * Prints registry name of feature and biome.
     * Prints the crashlog to latest.log as well.
     */
    public static void addStructureDetails(Biome biome, WorldGenRegion worldGenRegion, Structure<?> structureFeature, CrashReport crashreport) {
        DynamicRegistries dynamicRegistries = worldGenRegion.registryAccess();

        ResourceLocation structureID = null;
        ResourceLocation biomeID = null;

        try {
            structureID = ForgeRegistries.STRUCTURE_FEATURES.getKey(structureFeature);
            biomeID = dynamicRegistries.registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome);
        }
        catch (Throwable ignored) {
        }


        // Add extra info to the crash report file.
        // Note, only structures can do the details part as configuredfeatures always says the ConfiguredFeature class.
        crashreport.getSystemDetails()
                .setDetail("\n****************** Blame Report " + Blame.VERSION + " ******************",
                        "\n\n Structure Name : " + structureFeature.getFeatureName() + // Never null
                                "\n Structure Registry Name : " + (structureID != null ? structureID : "Structure is not registered somehow. Yell at the mod author when found to register their structures!") +
                                "\n Structure Details : " + structureFeature.toString() +
                                "\n Biome Registry Name : " + (biomeID != null ? biomeID : "Wait what? How is the biome not registered and has no registry name!?!? This should be impossible!!!"));

        // Log it to the latest.log file as well.
        Blame.LOGGER.log(Level.ERROR, crashreport.getFriendlyReport());
    }
}
