package com.telepathicgrunt.blame.mixin;

import net.minecraft.entity.EntityClassification;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Random;

import static com.telepathicgrunt.blame.main.WorldEntitySpawnerBlame.addMobCrashDetails;

/* @author - TelepathicGrunt
 *
 * Detect what mob is crashing the game with a 0 or negative list mob.
 *
 * LGPLv3
 */
@Mixin(WorldEntitySpawner.class)
public class WorldEntitySpawnerMixin {

    @Inject(method = "getRandomSpawnMobAt(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/gen/feature/structure/StructureManager;Lnet/minecraft/world/gen/ChunkGenerator;Lnet/minecraft/entity/EntityClassification;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/MobSpawnInfo$Spawners;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/WeightedRandom;getRandomItem(Ljava/util/Random;Ljava/util/List;)Lnet/minecraft/util/WeightedRandom$Item;", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private static void blame_checkIfMobSpawnWillCrash1(ServerWorld serverWorld, StructureManager structureManager,
                                                        ChunkGenerator chunkGenerator, EntityClassification entityClassification,
                                                        Random random, BlockPos pos, CallbackInfoReturnable<MobSpawnInfo.Spawners> cir,
                                                        Biome biome, List<MobSpawnInfo.Spawners> list) {
        addMobCrashDetails(serverWorld, entityClassification, pos, biome, list);
    }

    @Inject(method = "spawnMobsForChunkGeneration(Lnet/minecraft/world/IServerWorld;Lnet/minecraft/world/biome/Biome;IILjava/util/Random;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/WeightedRandom;getRandomItem(Ljava/util/Random;Ljava/util/List;)Lnet/minecraft/util/WeightedRandom$Item;", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private static void blame_checkIfMobSpawnWillCrash2(IServerWorld serverWorld, Biome biome,
                                                        int chunkX, int chunkZ, Random random,
                                                        CallbackInfo ci, MobSpawnInfo mobspawninfo,
                                                        List<MobSpawnInfo.Spawners> list) {
        addMobCrashDetails(serverWorld, EntityClassification.CREATURE, chunkX, chunkZ, biome, list);
    }
}
