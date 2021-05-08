package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.BiomeBlame;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
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
	@Inject(method = "generate(Lnet/minecraft/world/gen/feature/structure/StructureManager;Lnet/minecraft/world/gen/ChunkGenerator;Lnet/minecraft/world/gen/WorldGenRegion;JLnet/minecraft/util/SharedSeedRandom;Lnet/minecraft/util/math/BlockPos;)V",
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/crash/CrashReport;addCategory(Ljava/lang/String;)Lnet/minecraft/crash/CrashReportCategory;", ordinal = 1),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void addFeatureDetails(StructureManager structureManager, ChunkGenerator chunkGenerator,
								   WorldGenRegion worldGenRegion, long seed, SharedSeedRandom random, BlockPos pos,
								   CallbackInfo ci, List<List<Supplier<ConfiguredFeature<?, ?>>>> GenerationStageList,
								   int numOfGenerationStage, int generationStageIndex, int configuredFeatureIndex,
								   Iterator<ConfiguredFeature<?, ?>> var12, Supplier<ConfiguredFeature<?, ?>> supplier,
								   ConfiguredFeature<?, ?> configuredfeature, Exception exception, CrashReport crashreport)
	{
		BiomeBlame.addFeatureDetails((Biome)(Object)this, worldGenRegion, configuredfeature, crashreport);
	}


	/**
	 * Place blame on broke structures during worldgen.
	 * Prints registry name of feature and biome.
	 * Prints the crashlog to latest.log as well.
	 */
	@Inject(method = "generate(Lnet/minecraft/world/gen/feature/structure/StructureManager;Lnet/minecraft/world/gen/ChunkGenerator;Lnet/minecraft/world/gen/WorldGenRegion;JLnet/minecraft/util/SharedSeedRandom;Lnet/minecraft/util/math/BlockPos;)V",
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/crash/CrashReport;addCategory(Ljava/lang/String;)Lnet/minecraft/crash/CrashReportCategory;", ordinal = 0),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void addStructureDetails(StructureManager structureManager, ChunkGenerator chunkGenerator,
									 WorldGenRegion worldGenRegion, long seed, SharedSeedRandom random, BlockPos pos,
									 CallbackInfo ci, List<List<Supplier<ConfiguredFeature<?, ?>>>> list,
									 int numOfGenerationStage, int generationStageIndex, int configuredFeatureIndex,
									 Iterator<Structure<?>> var12, Structure<?> structureFeature,
									 int chunkX, int chunkZ, int ChunkXPos, int ChunkZPos,
									 Exception exception, CrashReport crashreport)
	{
		BiomeBlame.addStructureDetails((Biome)(Object)this, worldGenRegion, structureFeature, crashreport);
	}
}
