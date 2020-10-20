package com.telepathicgrunt.blame.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

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
	public static void addFeatureDetails(Biome biome, ChunkRegion chunkRegion,
										 ConfiguredFeature<?, ?> configuredFeature, CrashReport crashreport)
	{
		DynamicRegistryManager dynamicRegistryManager = chunkRegion.getRegistryManager();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		Identifier configuredFeatureID = dynamicRegistryManager.get(Registry.CONFIGURED_FEATURE_WORLDGEN).getId(configuredFeature);
		Identifier biomeID = dynamicRegistryManager.get(Registry.BIOME_KEY).getId(biome);
		Optional<JsonElement> configuredFeatureJSON = ConfiguredFeature.REGISTRY_CODEC.encode(() -> configuredFeature, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left();

		// Add extra info to the crash report file.
		crashreport.getSystemDetailsSection()
				.add("\n****************** Blame Report ******************",
					"\n\n ConfiguredFeature Registry Name : " + (configuredFeatureID != null ? configuredFeatureID : "Has no identifier as it was not registered... go yell at the mod owner when you find them! lol") +
						"\n Biome Registry Name : " + (biomeID != null ? biomeID : "Wait what? How is the biome not registered and has no registry name!?!? This should be impossible!!!") +
						"\n\n JSON info : " + (configuredFeatureJSON.isPresent() ? gson.toJson(configuredFeatureJSON.get()) : "Failed to get JSON somehow.") + "\n\n");

		// Log it to the latest.log file as well.
		Blame.LOGGER.log(Level.ERROR, crashreport.getMessage());
	}


	/**
	 * Place blame on broke structures during worldgen.
	 * Prints registry name of structure and biome.
	 * Prints the crashlog to latest.log as well.
	 */
	public static void addStructureDetails(Biome biome, ChunkRegion chunkRegion,
										   StructureFeature<?> structureFeature, CrashReport crashreport)
	{
		DynamicRegistryManager dynamicRegistryManager = chunkRegion.getRegistryManager();

		Identifier structureID = dynamicRegistryManager.get(Registry.STRUCTURE_FEATURE_KEY).getId(structureFeature);
		Identifier biomeID = dynamicRegistryManager.get(Registry.BIOME_KEY).getId(biome);

		// Add extra info to the crash report file.
		// Note, only structures can do the details part as configuredfeatures always says the ConfiguredFeature class.
		crashreport.getSystemDetailsSection()
				.add("\n****************** Blame Report ******************",
						"\n\n Structure Name : " + structureFeature.getName() + // Never null
						"\n Structure Registry Name : " + (structureID != null ? structureID : "Structure is not registered somehow. Yell at the mod author when found to register their structures!") +
						"\n Structure Details : " + structureFeature.toString() +
						"\n Biome Registry Name : " + (biomeID != null ? biomeID : "Wait what? How is the biome not registered and has no registry name!?!? This should be impossible!!!"));

		// Log it to the latest.log file as well.
		Blame.LOGGER.log(Level.ERROR, crashreport.getMessage());
	}
}
