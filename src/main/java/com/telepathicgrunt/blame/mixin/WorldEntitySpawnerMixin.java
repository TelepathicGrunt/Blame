package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.BiomeBlame;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static com.telepathicgrunt.blame.main.WeightedListBlame.addFeatureDetails;

/* @author - TelepathicGrunt
 *
 * Detect what mob is crashing the game with a 0 or negative list mob.
 *
 * LGPLv3
 */
@Mixin(WorldEntitySpawner.class)
public class WorldEntitySpawnerMixin {
	/**
	 * Place blame on broke feature during worldgen.
	 * Prints registry name of feature and biome.
	 * Prints the crashlog to latest.log as well.
	 */
	@Inject(method = "func_234977_a_(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/gen/feature/structure/StructureManager;Lnet/minecraft/world/gen/ChunkGenerator;Lnet/minecraft/entity/EntityClassification;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/MobSpawnInfo$Spawners;",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/WeightedRandom;getRandomItem(Ljava/util/Random;Ljava/util/List;)Lnet/minecraft/util/WeightedRandom$Item;", ordinal = 1),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private static void checkIfMobSpawnWillCrash(ServerWorld serverWorld, StructureManager structureManager,
										  ChunkGenerator chunkGenerator, EntityClassification entityClassification,
										  Random random, BlockPos pos, CallbackInfoReturnable<MobSpawnInfo.Spawners> cir,
										  Biome biome, List<MobSpawnInfo.Spawners> list)
	{
		addFeatureDetails(serverWorld, entityClassification, pos, biome, list);
	}

}
