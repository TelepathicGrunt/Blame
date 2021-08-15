package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.MissingNBTBlame;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/* @author - TelepathicGrunt
 *
 * Make it so TemplateManager actually states what nbt file was unable to be found.
 *
 * LGPLv3
 */
@Mixin(StructureManager.class)
public class StructureManagerMixin {

    @Inject(method = "getStructureOrBlank(Lnet/minecraft/util/Identifier;)Lnet/minecraft/structure/Structure;",
            at = @At(value = "RETURN"))
    private void blame_addMissingnbtDetails1(Identifier miniRL, CallbackInfoReturnable<Structure> cir) {
        if (cir.getReturnValue() == null) {
            MissingNBTBlame.addMissingNbtDetails(miniRL);
        }
    }

    @Inject(method = "getStructure(Lnet/minecraft/util/Identifier;)Ljava/util/Optional;",
            at = @At(value = "RETURN"))
    private void blame_addMissingnbtDetails2(Identifier miniRL, CallbackInfoReturnable<Optional<Structure>> cir) {
        if (!cir.getReturnValue().isPresent()) {
            MissingNBTBlame.addMissingNbtDetails(miniRL);
        }
    }
}
