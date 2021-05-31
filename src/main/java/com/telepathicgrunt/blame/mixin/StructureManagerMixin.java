package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.MissingNBTBlame;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.Identifier;
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
@Mixin(StructureManager.class)
public class StructureManagerMixin {

    @Inject(method = "getStructure(Lnet/minecraft/util/Identifier;)Lnet/minecraft/structure/Structure;",
            at = @At(value = "RETURN"))
    private void addMissingnbtDetails(Identifier miniRL, CallbackInfoReturnable<Structure> cir) {
        if (cir.getReturnValue() == null) {
            MissingNBTBlame.addMissingNbtDetails(miniRL);
        }
    }
}
