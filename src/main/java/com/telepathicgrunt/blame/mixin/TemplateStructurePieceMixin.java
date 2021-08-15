package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.TemplateStructurePieceBlame;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

/* @author - TelepathicGrunt
 *
 * Detect if someone's dummy structure piece is about to cause a crash
 *
 * LGPLv3
 */
@Mixin(TemplateStructurePiece.class)
public class TemplateStructurePieceMixin {

    @Shadow
    protected Template template;

    @Shadow
    protected PlacementSettings placeSettings;

    @Inject(method = "postProcess(Lnet/minecraft/world/ISeedReader;Lnet/minecraft/world/gen/feature/structure/StructureManager;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/MutableBoundingBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)Z",
            at = @At(value = "HEAD"))
    private void blame_addMissingnbtDetails(ISeedReader world, StructureManager structureManager,
                                            ChunkGenerator chunkGenerator, Random random,
                                            MutableBoundingBox boundingBox, ChunkPos chunkPos,
                                            BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (placeSettings == null) {
            TemplateStructurePieceBlame.printAboutToCrashBlame((TemplateStructurePiece)(Object)this);
        }

        if (template == null) {
            TemplateStructurePieceBlame.printAboutToCrashBlame2((TemplateStructurePiece)(Object)this);
        }
    }
}
