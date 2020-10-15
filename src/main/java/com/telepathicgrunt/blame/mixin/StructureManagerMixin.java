package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.Blame;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

/* @author - TelepathicGrunt
 *
 * Make it so TemplateManager actually states what nbt file was unable to be found.
 *
 * LGPLv3
 */
@Mixin(StructureManager.class)
public class StructureManagerMixin {

	// Prevent log spam if one mod keeps attempting to get the missing nbt file.
	@Unique
	private static final Set<String> PRINTED_RLS = new HashSet<>();

	@Inject(method = "loadStructureFromResource(Lnet/minecraft/util/Identifier;)Lnet/minecraft/structure/Structure;",
			at = @At(value = "RETURN"))
	private void addMissingnbtDetails(Identifier miniRL, CallbackInfoReturnable<Structure> cir)
	{
		if(cir.getReturnValue() == null){
			Identifier fullRL = new Identifier(miniRL.getNamespace(), "structures/" + miniRL.getPath() + ".nbt");
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
