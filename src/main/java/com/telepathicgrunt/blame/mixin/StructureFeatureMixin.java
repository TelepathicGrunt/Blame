package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.StructureFeatureBlame;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* @author - TelepathicGrunt
 *
 * Detect who messed up their structure spacing/separation values
 *
 * LGPLv3
 */
@Mixin(StructureFeature.class)
public class StructureFeatureMixin {

    @Inject(method = "getStartChunk(Lnet/minecraft/world/gen/chunk/StructureConfig;JLnet/minecraft/world/gen/ChunkRandom;II)Lnet/minecraft/util/math/ChunkPos;",
            at = @At(value = "HEAD"))
    private void blame_checkSpacing(StructureConfig config, long worldSeed, ChunkRandom placementRandom, int chunkX, int chunkY, CallbackInfoReturnable<ChunkPos> cir) {
        if (config.getSpacing() == 0 || config.getSpacing() - config.getSeparation() <= 0) {
            StructureFeatureBlame.printStructureSpacingBlame((StructureFeature<?>) (Object) this, config);
        }
    }
}
