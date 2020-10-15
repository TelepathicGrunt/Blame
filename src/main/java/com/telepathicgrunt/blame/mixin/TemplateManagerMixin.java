package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.Blame;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashSet;
import java.util.Set;

/* @author - TelepathicGrunt
 *
 * Make it so TemplateManager actually states what nbt file was unable to be found.
 *
 * LGPLv3
 */
@Mixin(TemplateManager.class)
public class TemplateManagerMixin {

	// Prevent log spam if one mod keeps attempting to get the missing nbt file.
	@Unique
	private static final Set<String> PRINTED_RLS = new HashSet<>();

	@Inject(method = "loadTemplateResource(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/world/gen/feature/template/Template;",
			at = @At(value = "RETURN"))
	private void addMissingnbtDetails(ResourceLocation miniRL, CallbackInfoReturnable<Template> cir)
	{
		if(cir.getReturnValue() == null){
			ResourceLocation fullRL = new ResourceLocation(miniRL.getNamespace(), "structures/" + miniRL.getPath() + ".nbt");
			if(PRINTED_RLS.contains(fullRL.toString())) return;

			// Add extra info to the log file.
			String errorReport = "\n****************** Blame Report ******************" +
					"\n\n Failed to load nbt file from : " + fullRL +
					"\n Most common cause is that the nbt file is not actually at that location." +
					"\n Please let the mod author know about this so they can move their nbt file to the correct place." +
					"\n A common mistake is putting the nbt file in the asset folder when it needs to go in data/structures folder.\n";
			Blame.LOGGER.log(Level.ERROR, errorReport);

			PRINTED_RLS.add(fullRL.toString());
		}
	}
}
