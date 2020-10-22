package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.MissingNBTBlame;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* @author - TelepathicGrunt
 *
 * Make it so StructureManager actually states what nbt file was unable to be found.
 *
 * LGPLv3
 */
@Mixin(JigsawPattern.class)
public class JigsawPatternMixin {


	@Inject(method = "getMaxSize(Lnet/minecraft/world/gen/feature/template/TemplateManager;)I",
			at = @At(value = "HEAD"))
	private void tempPool(TemplateManager templateManager, CallbackInfoReturnable<Integer> cir)
	{
		MissingNBTBlame.CALLING_POOL = ((JigsawPattern)(Object)this).getName();
	}

	@Inject(method = "getMaxSize(Lnet/minecraft/world/gen/feature/template/TemplateManager;)I",
			at = @At(value = "TAIL"))
	private void tempPoolClear(TemplateManager templateManager, CallbackInfoReturnable<Integer> cir)
	{
		MissingNBTBlame.CALLING_POOL = null;
	}
}
