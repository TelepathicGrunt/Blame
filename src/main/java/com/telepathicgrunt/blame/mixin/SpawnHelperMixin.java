package com.telepathicgrunt.blame.mixin;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;
import java.util.Random;

import static com.telepathicgrunt.blame.main.SpawnHelperBlame.addMobCrashDetails;

/* @author - TelepathicGrunt
 *
 * Detect what mob is crashing the game with a 0 or negative list mob.
 *
 * LGPLv3
 */
@Mixin(SpawnHelper.class)
public class SpawnHelperMixin {

    @Redirect(method = "pickRandomSpawnEntry(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/entity/SpawnGroup;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Ljava/util/Optional;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/Pool;getOrEmpty(Ljava/util/Random;)Ljava/util/Optional;"))
    private static Optional<SpawnSettings.SpawnEntry> checkIfMobSpawnWillCrash(Pool<SpawnSettings.SpawnEntry> pool, Random random,
                                                                                     ServerWorld serverWorld, StructureAccessor structureAccessor,
                                                                                     ChunkGenerator chunkGenerator, SpawnGroup spawnGroup,
                                                                                     Random random2, BlockPos pos) {
        Biome biome = serverWorld.getBiome(pos);
        addMobCrashDetails(serverWorld, spawnGroup, pos, biome, pool);
        return pool.getOrEmpty(random);
    }

    @Inject(method = "populateEntities(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/biome/Biome;Lnet/minecraft/util/math/ChunkPos;Ljava/util/Random;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/Pool;getOrEmpty(Ljava/util/Random;)Ljava/util/Optional;", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private static void checkIfMobSpawnWillCrash2(ServerWorldAccess world, Biome biome, ChunkPos chunkPos, Random random, CallbackInfo ci, SpawnSettings spawnSettings, Pool<SpawnSettings.SpawnEntry> pool) {
        addMobCrashDetails(world, chunkPos, biome, pool);
    }
}
