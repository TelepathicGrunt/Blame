package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.MissingNBTBlame;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/* @author - TelepathicGrunt
 *
 * Make it so TemplateManager actually states what nbt file was unable to be found.
 *
 * LGPLv3
 */
@Mixin(targets = "net.minecraft.world.gen.feature.jigsaw.JigsawManager$Assembler")
public class JigsawManagerAssemblerMixin {


	@Inject(method = "func_236831_a_(Lnet/minecraft/world/gen/feature/structure/AbstractVillagePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IIZ)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Rotation;shuffledRotations(Ljava/util/Random;)Ljava/util/List;"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void storeCurrentPool2(AbstractVillagePiece abstractVillagePiece, MutableObject<VoxelShape> voxelShapeMutableObject,
								   int i1, int i2, boolean b, CallbackInfo ci, JigsawPiece jigsawpiece)
	{
		if(jigsawpiece instanceof SingleJigsawPiece && ((SingleJigsawPieceAccessor)jigsawpiece).getTemplateRL().left().isPresent())
		{
			MissingNBTBlame.CALLING_POOL = ((SingleJigsawPieceAccessor) jigsawpiece).getTemplateRL().left().get();
		}
	}

	@Inject(method = "func_236831_a_(Lnet/minecraft/world/gen/feature/structure/AbstractVillagePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IIZ)V",
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/gen/feature/jigsaw/JigsawManager$IPieceFactory;create(Lnet/minecraft/world/gen/feature/template/TemplateManager;Lnet/minecraft/world/gen/feature/jigsaw/JigsawPiece;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/util/Rotation;Lnet/minecraft/util/math/MutableBoundingBox;)Lnet/minecraft/world/gen/feature/structure/AbstractVillagePiece;"))
	private void storeCurrentPool2(AbstractVillagePiece abstractVillagePiece, MutableObject<VoxelShape> voxelShapeMutableObject,
								   int i, int i1, boolean b, CallbackInfo ci)
	{
		MissingNBTBlame.CALLING_POOL = null;
	}
}
