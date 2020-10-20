package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.MissingNBTBlame;
import javafx.util.Pair;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
								   int i1, int i2, boolean b, CallbackInfo ci, JigsawPiece jigsawpiece, BlockPos blockpos,
								   Rotation rotation, JigsawPattern.PlacementBehaviour jigsawpattern$placementbehaviour,
								   boolean flag, MutableObject mutableobject, MutableBoundingBox mutableboundingbox, int i,
								   Iterator var14, Template.BlockInfo template$blockinfo, Direction direction, BlockPos blockpos1,
								   BlockPos blockpos2, int j, int k, ResourceLocation resourcelocation, Optional optional,
								   ResourceLocation resourcelocation1, Optional optional1, boolean flag1, MutableObject mutableobject1,
								   int l, List list, Iterator var29, JigsawPiece jigsawpiece1)
	{
		if(jigsawpiece instanceof SingleJigsawPiece && ((SingleJigsawPieceAccessor)jigsawpiece).getTemplateRL().left().isPresent() &&
			jigsawpiece1 instanceof SingleJigsawPiece && ((SingleJigsawPieceAccessor)jigsawpiece1).getTemplateRL().left().isPresent())
		{
			MissingNBTBlame.storeCurrentIdentifiers(new Pair<>(
					((SingleJigsawPieceAccessor) jigsawpiece).getTemplateRL().left().get(),
					((SingleJigsawPieceAccessor) jigsawpiece1).getTemplateRL().left().get()));
		}
		else{
			MissingNBTBlame.storeCurrentIdentifiers(null);
		}
	}
}
