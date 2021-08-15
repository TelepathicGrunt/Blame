package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.MissingNBTBlame;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Random;

/* @author - TelepathicGrunt
 *
 * Make it so TemplateManager actually states what nbt file was unable to be found.
 *
 * LGPLv3
 */
@Mixin(StructurePoolBasedGenerator.class)
public class StructurePoolBasedGeneratorMixin {

    @Inject(method = "method_30419(Lnet/minecraft/util/registry/DynamicRegistryManager;Lnet/minecraft/world/gen/feature/StructurePoolFeatureConfig;Lnet/minecraft/structure/pool/StructurePoolBasedGenerator$PieceFactory;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/structure/StructureManager;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/StructurePiecesHolder;Ljava/util/Random;ZZLnet/minecraft/world/HeightLimitView;)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/structure/pool/StructurePool;getRandomElement(Ljava/util/Random;)Lnet/minecraft/structure/pool/StructurePoolElement;"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private static void blame_storeCurrentPool(DynamicRegistryManager dynamicRegistryManager, StructurePoolFeatureConfig structurePoolFeatureConfig,
                                               StructurePoolBasedGenerator.PieceFactory pieceFactory, ChunkGenerator chunkGenerator,
                                               StructureManager structureManager, BlockPos blockPos, StructurePiecesHolder structurePiecesHolder,
                                               Random random, boolean bl, boolean bl2, HeightLimitView heightLimitView, CallbackInfo ci, List<PoolStructurePiece> list,
                                               Registry<StructurePool> mutableRegistry, BlockRotation blockRotation, StructurePool structurePool) {
        MissingNBTBlame.CALLING_POOL = structurePool.getId();
    }
}
