package com.telepathicgrunt.blame.mixin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.JsonOps;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.crash.CrashReport;
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
import java.util.function.Supplier;

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
								   Iterator var12, Supplier supplier, ConfiguredFeature<?, ?> configuredfeature,
								   Exception exception, CrashReport crashreport)
	{
		DynamicRegistries dynamicRegistries = worldGenRegion.getWorld().getWorldServer().func_241828_r();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		crashreport.getCategory()
				.addDetail("\n****************** Blame Report ******************",
					"\n\n ConfiguredFeature Registry Name : " + Objects.requireNonNull(dynamicRegistries.func_243612_b(Registry.field_243552_au).getKey(configuredfeature)) +
					"\n Biome Registry Name : " + Objects.requireNonNull(dynamicRegistries.func_243612_b(Registry.BIOME_KEY).getKey((Biome)(Object)this)) +
					"\n\n JSON info : " +
							(ConfiguredFeature.field_236264_b_.encode(() -> configuredfeature, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left().isPresent()
									? gson.toJson(ConfiguredFeature.field_236264_b_.encode(() -> configuredfeature, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left().get())
									: " failed to get json.") + "\n\n");

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
									 CallbackInfo ci, List list, int numOfGenerationStage, int generationStageIndex,
									 int configuredFeatureIndex, Iterator var12, Structure<?> structure,
									 int chunkX, int chunkZ, int ChunkXPos, int ChunkZPos,
									 Exception exception, CrashReport crashreport)
	{
		DynamicRegistries dynamicRegistries = worldGenRegion.getWorld().getWorldServer().func_241828_r();

		crashreport.getCategory()
				.addDetail("\n****************** Blame Report ******************",
				"\n\n Structure Name : " + structure.getStructureName() +
				"\n Structure Registry Name : " + Objects.requireNonNull(dynamicRegistries.func_243612_b(Registry.STRUCTURE_FEATURE_KEY).getKey(structure)) +
				"\n Biome Registry Name : " + Objects.requireNonNull(dynamicRegistries.func_243612_b(Registry.BIOME_KEY).getKey((Biome)(Object)this)) + "\n\n");

		Blame.LOGGER.log(Level.ERROR, crashreport.getCompleteReport());
	}
}
