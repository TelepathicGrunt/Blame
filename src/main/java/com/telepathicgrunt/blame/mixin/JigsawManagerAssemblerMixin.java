package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.MissingNBTBlame;
import com.telepathicgrunt.blame.main.MissingTemplatePoolBlame;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.template.Template;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

/* @author - TelepathicGrunt
 *
 * Make it so TemplateManager actually states what nbt file was unable to be found.
 *
 * LGPLv3
 */
@Mixin(targets = "net.minecraft.world.gen.feature.jigsaw.JigsawManager$Assembler")
public class JigsawManagerAssemblerMixin {


    @Inject(method = "tryPlacingChildren(Lnet/minecraft/world/gen/feature/structure/AbstractVillagePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IIZ)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Rotation;getShuffled(Ljava/util/Random;)Ljava/util/List;"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void blame_storeCurrentPool1(AbstractVillagePiece abstractVillagePiece, MutableObject<VoxelShape> voxelShapeMutableObject,
                                         int i1, int i2, boolean b, CallbackInfo ci, JigsawPiece jigsawpiece) {
        if (jigsawpiece instanceof SingleJigsawPiece && ((SingleJigsawPieceAccessor) jigsawpiece).blame_getTemplateRL().left().isPresent()) {
            MissingNBTBlame.CALLING_POOL = ((SingleJigsawPieceAccessor) jigsawpiece).blame_getTemplateRL().left().get();
        }
    }

    @Inject(method = "tryPlacingChildren(Lnet/minecraft/world/gen/feature/structure/AbstractVillagePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IIZ)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/gen/feature/jigsaw/JigsawManager$IPieceFactory;create(Lnet/minecraft/world/gen/feature/template/TemplateManager;Lnet/minecraft/world/gen/feature/jigsaw/JigsawPiece;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/util/Rotation;Lnet/minecraft/util/math/MutableBoundingBox;)Lnet/minecraft/world/gen/feature/structure/AbstractVillagePiece;"))
    private void blame_storeCurrentPool2(AbstractVillagePiece abstractVillagePiece, MutableObject<VoxelShape> voxelShapeMutableObject,
                                         int i, int i1, boolean b, CallbackInfo ci) {
        MissingNBTBlame.CALLING_POOL = null;
    }


    @Inject(method = "tryPlacingChildren(Lnet/minecraft/world/gen/feature/structure/AbstractVillagePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IIZ)V",
            at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", ordinal = 0, remap = false),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void blame_printMissingPoolDetails1(AbstractVillagePiece abstractVillagePiece, MutableObject<VoxelShape> voxelShapeMutableObject,
                                                int i1, int i2, boolean b, CallbackInfo ci, JigsawPiece structurePoolElement, BlockPos blockPos,
                                                Rotation blockRotation, JigsawPattern.PlacementBehaviour projection, boolean bl2,
                                                MutableObject<VoxelShape> mutableObject2, MutableBoundingBox blockBox, int i, Iterator<Template.BlockInfo> var14,
                                                Template.BlockInfo structureBlockInfo, Direction direction,
                                                BlockPos blockPos2, BlockPos blockPos3, int j, int k, ResourceLocation targetPoolId) {
        MissingTemplatePoolBlame.addEmptyPoolDetails(targetPoolId, ((SingleJigsawPieceAccessor) structurePoolElement).blame_getTemplateRL().left().orElse(null));
    }

    @Inject(method = "tryPlacingChildren(Lnet/minecraft/world/gen/feature/structure/AbstractVillagePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IIZ)V",
            at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", ordinal = 1),
            locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private void blame_printMissingPoolDetails2(AbstractVillagePiece abstractVillagePiece, MutableObject<VoxelShape> voxelShapeMutableObject,
                                                int i1, int i2, boolean b, CallbackInfo ci, JigsawPiece structurePoolElement, BlockPos blockPos,
                                                Rotation blockRotation, JigsawPattern.PlacementBehaviour projection, boolean bl2,
                                                MutableObject<VoxelShape> mutableObject2, MutableBoundingBox blockBox, int i, Iterator<Template.BlockInfo> var14,
                                                Template.BlockInfo structureBlockInfo, Direction direction,
                                                BlockPos blockPos2, BlockPos blockPos3, int j, int k, ResourceLocation targetPoolId) {
        MissingTemplatePoolBlame.addEmptyPoolDetails(targetPoolId, ((SingleJigsawPieceAccessor) structurePoolElement).blame_getTemplateRL().left().orElse(null));
    }
}
