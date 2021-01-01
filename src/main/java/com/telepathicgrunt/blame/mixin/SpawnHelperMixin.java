package com.telepathicgrunt.blame.mixin;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Random;

import static com.telepathicgrunt.blame.main.SpawnHelperBlame.addFeatureDetails;

/* @author - TelepathicGrunt
 *
 * Detect what mob is crashing the game with a 0 or negative list mob.
 *
 * LGPLv3
 */
@Mixin(SpawnHelper.class)
public class SpawnHelperMixin {

	@Inject(method = "pickRandomSpawnEntry(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/entity/SpawnGroup;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/SpawnSettings$SpawnEntry;",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/WeightedPicker;getRandom(Ljava/util/Random;Ljava/util/List;)Lnet/minecraft/util/collection/WeightedPicker$Entry;", ordinal = 0),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private static void checkIfMobSpawnWillCrash(ServerWorld serverWorld, StructureAccessor structureAccessor,
												 ChunkGenerator chunkGenerator, SpawnGroup spawnGroup,
												 Random random, BlockPos pos, CallbackInfoReturnable<SpawnSettings.SpawnEntry> cir,
												 Biome biome, List<SpawnSettings.SpawnEntry> list)
	{
		addFeatureDetails(serverWorld, spawnGroup, pos, biome, list);
	}
}
