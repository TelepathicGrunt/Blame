package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.MissingNBTBlame;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.pool.StructurePool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* @author - TelepathicGrunt
 *
 * Make it so TemplateManager actually states what nbt file was unable to be found.
 *
 * LGPLv3
 */
@Mixin(StructurePool.class)
public abstract class StructurePoolMixin {

	@Inject(method = "getHighestY(Lnet/minecraft/structure/StructureManager;)I",
			at = @At(value = "HEAD"))
	private void tempPool(StructureManager structureManager, CallbackInfoReturnable<Integer> cir)
	{
		MissingNBTBlame.CALLING_POOL = ((StructurePool)(Object)this).getId();
	}

	@Inject(method = "getHighestY(Lnet/minecraft/structure/StructureManager;)I",
			at = @At(value = "TAIL"))
	private void tempPoolClear(StructureManager structureManager, CallbackInfoReturnable<Integer> cir)
	{
		MissingNBTBlame.CALLING_POOL = null;
	}

}
