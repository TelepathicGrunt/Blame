package com.telepathicgrunt.blame.mixin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
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
@Mixin(Biome.class)
public class BiomeMixin {
	/**
	 * Place blame on broke feature during worldgen.
	 * Prints registry name of feature and biome.
	 * Prints the crashlog to latest.log as well.
	 */
	@Inject(method = "func_242427_a(Lnet/minecraft/world/gen/feature/structure/StructureManager;Lnet/minecraft/world/gen/ChunkGenerator;Lnet/minecraft/world/gen/WorldGenRegion;JLnet/minecraft/util/SharedSeedRandom;Lnet/minecraft/util/math/BlockPos;)V",
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/crash/CrashReport;makeCategory(Ljava/lang/String;)Lnet/minecraft/crash/CrashReportCategory;", ordinal = 1),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void addFeatureDetails(StructureManager structureManager, ChunkGenerator chunkGenerator,
								   WorldGenRegion worldGenRegion, long seed, SharedSeedRandom random, BlockPos pos,
								   CallbackInfo ci, List<List<Supplier<ConfiguredFeature<?, ?>>>> GenerationStageList,
								   int numOfGenerationStage, int generationStageIndex, int configuredFeatureIndex,
								   Iterator<ConfiguredFeature<?, ?>> var12, Supplier<ConfiguredFeature<?, ?>> supplier, ConfiguredFeature<?, ?> configuredfeature,
								   Exception exception, CrashReport crashreport)
	{
		DynamicRegistries dynamicRegistries = worldGenRegion.func_241828_r();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		ResourceLocation configuredFeatureID = dynamicRegistries.func_243612_b(Registry.field_243552_au).getKey(configuredfeature);
		ResourceLocation biomeID = dynamicRegistries.func_243612_b(Registry.BIOME_KEY).getKey((Biome)(Object)this);
		Optional<JsonElement> configuredFeatureJSON = ConfiguredFeature.field_236264_b_.encode(() -> configuredfeature, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left();

		// Add extra info to the crash report file.
		crashreport.getCategory()
				.addDetail("\n****************** Blame Report ******************",
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
	@Inject(method = "func_242427_a(Lnet/minecraft/world/gen/feature/structure/StructureManager;Lnet/minecraft/world/gen/ChunkGenerator;Lnet/minecraft/world/gen/WorldGenRegion;JLnet/minecraft/util/SharedSeedRandom;Lnet/minecraft/util/math/BlockPos;)V",
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/crash/CrashReport;makeCategory(Ljava/lang/String;)Lnet/minecraft/crash/CrashReportCategory;", ordinal = 0),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void addStructureDetails(StructureManager structureManager, ChunkGenerator chunkGenerator,
									 WorldGenRegion worldGenRegion, long seed, SharedSeedRandom random, BlockPos pos,
									 CallbackInfo ci, List<List<Supplier<ConfiguredFeature<?, ?>>>> list,
									 int numOfGenerationStage, int generationStageIndex, int configuredFeatureIndex,
									 Iterator<Structure<?>> var12, Structure<?> structureFeature,
									 int chunkX, int chunkZ, int ChunkXPos, int ChunkZPos,
									 Exception exception, CrashReport crashreport)
	{
		DynamicRegistries dynamicRegistries = worldGenRegion.func_241828_r();

		ResourceLocation structureID = dynamicRegistries.func_243612_b(Registry.STRUCTURE_FEATURE_KEY).getKey(structureFeature);
		ResourceLocation biomeID = dynamicRegistries.func_243612_b(Registry.BIOME_KEY).getKey((Biome)(Object)this);

		// Add extra info to the crash report file.
		// Note, only structures can do the details part as configuredfeatures always says the ConfiguredFeature class.
		crashreport.getCategory()
				.addDetail("\n****************** Blame Report ******************",
						"\n\n Structure Name : " + structureFeature.getStructureName() + // Never null
						"\n Structure Registry Name : " + (structureID != null ? structureID : "Structure is not registered somehow. Yell at the mod author when found to register their structures!") +
						"\n Structure Details : " + structureFeature.toString() +
						"\n Biome Registry Name : " + (biomeID != null ? biomeID : "Wait what? How is the biome not registered and has no registry name!?!? This should be impossible!!!"));

		// Log it to the latest.log file as well.
		Blame.LOGGER.log(Level.ERROR, crashreport.getCompleteReport());
	}
}
