package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.MissingNBTBlame;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
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
@Mixin(TemplateManager.class)
public class TemplateManagerMixin {

	@Inject(method = "loadTemplateResource(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/world/gen/feature/template/Template;",
			at = @At(value = "RETURN"))
	private void addMissingnbtDetails(ResourceLocation miniRL, CallbackInfoReturnable<Template> cir)
	{
		if(cir.getReturnValue() == null){
			MissingNBTBlame.addMissingnbtDetails(miniRL);
		}
	}
}
