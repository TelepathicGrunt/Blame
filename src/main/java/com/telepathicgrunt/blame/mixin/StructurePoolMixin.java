package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.MissingNBTBlame;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Random;

/* @author - TelepathicGrunt
 *
 * Make it so TemplateManager actually states what nbt file was unable to be found.
 *
 * LGPLv3
 */
@Mixin(StructurePool.class)
public class StructurePoolMixin {

	@Unique
	private static StructurePool CALLING_POOL = null;

	@Inject(method = "Lnet/minecraft/structure/pool/StructurePool;getHighestY(Lnet/minecraft/structure/StructureManager;)I",
			at = @At(value = "HEAD"))
	private void tempPool(StructureManager structureManager, CallbackInfoReturnable<Integer> cir)
	{
		CALLING_POOL = (StructurePool)(Object)this;
	}

	@Dynamic
	@Inject(method = "method_19310(Lnet/minecraft/structure/StructureManager;Lnet/minecraft/structure/pool/StructurePoolElement;)I",
			at = @At(value = "HEAD"))
	private static void storeCurrentPool(StructureManager structureManager, StructurePoolElement structurePoolElement, CallbackInfoReturnable<Integer> cir)
	{
		if(structurePoolElement instanceof SinglePoolElement && ((SinglePoolElementAccessor)structurePoolElement).getTemplateID().left().isPresent()){
			MissingNBTBlame.storeCurrentIdentifiers(new Pair<>(CALLING_POOL.getId(), ((SinglePoolElementAccessor) structurePoolElement).getTemplateID().left().get()));
		}
		else{
			MissingNBTBlame.storeCurrentIdentifiers(null);
		}
	}
}
