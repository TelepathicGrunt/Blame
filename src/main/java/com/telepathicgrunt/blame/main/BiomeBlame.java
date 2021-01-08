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
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;

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
	public static void addFeatureDetails(Biome biome, WorldGenRegion worldGenRegion, ConfiguredFeature<?, ?> configuredFeature, CrashReport crashreport)
	{
		DynamicRegistries dynamicRegistries = worldGenRegion.func_241828_r();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		ResourceLocation configuredFeatureID = dynamicRegistries.getRegistry(Registry.CONFIGURED_FEATURE_KEY).getKey(configuredFeature);
		ResourceLocation biomeID = dynamicRegistries.getRegistry(Registry.BIOME_KEY).getKey(biome);
		Optional<JsonElement> configuredFeatureJSON = ConfiguredFeature.field_236264_b_.encode(() -> configuredFeature, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left();

		// Add extra info to the crash report file.
		crashreport.getCategory()
				.addDetail("\n****************** Blame Report " + Blame.VERSION + " ******************",
					"\n\n ConfiguredFeature Registry Name : " + (configuredFeatureID != null ? configuredFeatureID : "Has no identifier as it was not registered... go yell at the mod owner when you find them! lol") +
						"\n Biome Registry Name : " + (biomeID != null ? biomeID : "Wait what? How is the biome not registered and has no registry name!?!? This should be impossible!!!") +
						"\n\n JSON info : " + (configuredFeatureJSON.isPresent() ? gson.toJson(configuredFeatureJSON.get()) : "Failed to get JSON somehow.") + "\n\n");

		// Log it to the latest.log file as well.
		Blame.LOGGER.log(Level.ERROR, crashreport.getCompleteReport());
	}


	/**
	 * Place blame on broke structures during worldgen.
	 * Prints registry name of feature and biome.
	 * Prints the crashlog to latest.log as well.
	 */
	public static void addStructureDetails(Biome biome, WorldGenRegion worldGenRegion, Structure<?> structureFeature, CrashReport crashreport)
	{
		DynamicRegistries dynamicRegistries = worldGenRegion.func_241828_r();

		ResourceLocation structureID = ForgeRegistries.STRUCTURE_FEATURES.getKey(structureFeature);
		ResourceLocation biomeID = dynamicRegistries.getRegistry(Registry.BIOME_KEY).getKey(biome);

		// Add extra info to the crash report file.
		// Note, only structures can do the details part as configuredfeatures always says the ConfiguredFeature class.
		crashreport.getCategory()
				.addDetail("\n****************** Blame Report " + Blame.VERSION + " ******************",
						"\n\n Structure Name : " + structureFeature.getStructureName() + // Never null
						"\n Structure Registry Name : " + (structureID != null ? structureID : "Structure is not registered somehow. Yell at the mod author when found to register their structures!") +
						"\n Structure Details : " + structureFeature.toString() +
						"\n Biome Registry Name : " + (biomeID != null ? biomeID : "Wait what? How is the biome not registered and has no registry name!?!? This should be impossible!!!"));

		// Log it to the latest.log file as well.
		Blame.LOGGER.log(Level.ERROR, crashreport.getCompleteReport());
	}
}
