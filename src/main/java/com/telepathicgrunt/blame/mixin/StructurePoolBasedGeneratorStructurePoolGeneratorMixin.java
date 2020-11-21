package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.MissingNBTBlame;
import com.telepathicgrunt.blame.main.MissingTemplatePoolBlame;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
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
@Mixin(targets = "net.minecraft.structure.pool.StructurePoolBasedGenerator$StructurePoolGenerator")
public class StructurePoolBasedGeneratorStructurePoolGeneratorMixin {


	@Inject(method = "generatePiece(Lnet/minecraft/structure/PoolStructurePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IIZ)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/BlockRotation;randomRotationOrder(Ljava/util/Random;)Ljava/util/List;"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void storeCurrentPool2(PoolStructurePiece piece, MutableObject<VoxelShape> mutableObject, int minY, int currentSize,
								   boolean bl, CallbackInfo ci, StructurePoolElement structurePoolElement)
	{
		if(structurePoolElement instanceof SinglePoolElement && ((SinglePoolElementAccessor)structurePoolElement).getTemplateID().left().isPresent())
		{
			MissingNBTBlame.CALLING_POOL = ((SinglePoolElementAccessor) structurePoolElement).getTemplateID().left().get();
		}
	}

	@Inject(method = "generatePiece(Lnet/minecraft/structure/PoolStructurePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IIZ)V",
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/structure/pool/StructurePoolBasedGenerator$PieceFactory;create(Lnet/minecraft/structure/StructureManager;Lnet/minecraft/structure/pool/StructurePoolElement;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/util/BlockRotation;Lnet/minecraft/util/math/BlockBox;)Lnet/minecraft/structure/PoolStructurePiece;"))
	private void storeCurrentPool3(PoolStructurePiece piece, MutableObject<VoxelShape> mutableObject,
								   int minY, int currentSize, boolean bl, CallbackInfo ci)
	{
		MissingNBTBlame.CALLING_POOL = null;
	}


	
	@Inject(method = "generatePiece(Lnet/minecraft/structure/PoolStructurePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IIZ)V",
			at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", ordinal = 0),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void printMissingPoolDetails1(PoolStructurePiece piece, MutableObject<VoxelShape> mutableObject, int minY, int currentSize,
										  boolean bl, CallbackInfo ci, StructurePoolElement structurePoolElement, BlockPos blockPos,
										  BlockRotation blockRotation, StructurePool.Projection projection, boolean bl2,
										  MutableObject<VoxelShape> mutableObject2, BlockBox blockBox, int i, Iterator<Structure.StructureBlockInfo> var14,
										  Structure.StructureBlockInfo structureBlockInfo, Direction direction,
										  BlockPos blockPos2, BlockPos blockPos3, int j, int k, Identifier targetPoolId)
	{
		MissingTemplatePoolBlame.addEmptyPoolDetails(targetPoolId, ((SinglePoolElementAccessor) structurePoolElement).getTemplateID().left().orElse(null));
	}

	@Inject(method = "generatePiece(Lnet/minecraft/structure/PoolStructurePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IIZ)V",
			at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", ordinal = 1),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void printMissingPoolDetails2(PoolStructurePiece piece, MutableObject<VoxelShape> mutableObject, int minY, int currentSize,
										  boolean bl, CallbackInfo ci, StructurePoolElement structurePoolElement, BlockPos blockPos,
										  BlockRotation blockRotation, StructurePool.Projection projection, boolean bl2,
										  MutableObject<VoxelShape> mutableObject2, BlockBox blockBox, int i, Iterator<Structure.StructureBlockInfo> var14,
										  Structure.StructureBlockInfo structureBlockInfo, Direction direction,
										  BlockPos blockPos2, BlockPos blockPos3, int j, int k, Identifier targetPoolId)
	{
		MissingTemplatePoolBlame.addEmptyPoolDetails(targetPoolId, ((SinglePoolElementAccessor) structurePoolElement).getTemplateID().left().orElse(null));
	}
}
