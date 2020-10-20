package com.telepathicgrunt.blame.mixin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.telepathicgrunt.blame.Blame;
import com.telepathicgrunt.blame.main.BiomeBlame;
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
@Mixin(Biome.class)
public class BiomeMixin {
	/**
	 * Place blame on broke feature during worldgen.
	 * Prints registry name of feature and biome.
	 * Prints the crashlog to latest.log as well.
	 */
	@Inject(method = "generateFeatureStep(Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/ChunkRegion;JLnet/minecraft/world/gen/ChunkRandom;Lnet/minecraft/util/math/BlockPos;)V",
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;", ordinal = 1),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void addFeatureDetails(StructureAccessor structureAccessor, ChunkGenerator chunkGenerator,
								   ChunkRegion chunkRegion, long seed, ChunkRandom random, BlockPos pos,
								   CallbackInfo ci, List<List<Supplier<ConfiguredFeature<?, ?>>>> GenerationStageList,
								   int numOfGenerationSteps, int generationStepIndex, int configuredFeatureIndex,
								   Iterator<ConfiguredFeature<?, ?>> var12, Supplier<ConfiguredFeature<?, ?>> supplier,
								   ConfiguredFeature<?, ?> configuredFeature, Exception exception, CrashReport crashreport)
	{
		BiomeBlame.addFeatureDetails((Biome)(Object)this, chunkRegion, configuredFeature, crashreport);
	}


	/**
	 * Place blame on broke structures during worldgen.
	 * Prints registry name of structure and biome.
	 * Prints the crashlog to latest.log as well.
	 */
	@Inject(method = "generateFeatureStep(Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/ChunkRegion;JLnet/minecraft/world/gen/ChunkRandom;Lnet/minecraft/util/math/BlockPos;)V",
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;", ordinal = 0),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void addStructureDetails(StructureAccessor structureAccessor, ChunkGenerator chunkGenerator,
									 ChunkRegion chunkRegion, long seed, ChunkRandom random, BlockPos pos,
									 CallbackInfo ci, List<List<Supplier<ConfiguredFeature<?, ?>>>> list,
									 int numOfGenerationSteps, int generationStepIndex, int configuredFeatureIndex,
									 List<List<Supplier<ConfiguredFeature<?, ?>>>> list2,
									 Iterator<StructureFeature<?>> structureFeatureIterator, StructureFeature<?> structureFeature,
									 Exception exception, CrashReport crashreport)
	{
		BiomeBlame.addStructureDetails((Biome)(Object)this, chunkRegion, structureFeature, crashreport);
	}
}
