package com.telepathicgrunt.blame.mixin;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.blame.main.MissingNBTBlame;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.util.math.BlockBox;
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
@Mixin(SinglePoolElement.class)
public class SinglePoolElementMixin {

    @Inject(method = "method_27233(Lnet/minecraft/structure/StructureManager;)Lnet/minecraft/structure/Structure;",
            at = @At(value = "HEAD"))
    private void storeCurrentPool(StructureManager structureManager, CallbackInfoReturnable<BlockBox> cir) {
        if (MissingNBTBlame.CALLING_POOL != null & ((SinglePoolElementAccessor) this).blame_getTemplateID().left().isPresent()) {
            MissingNBTBlame.storeCurrentIdentifiers(new Pair<>(MissingNBTBlame.CALLING_POOL, ((SinglePoolElementAccessor) this).blame_getTemplateID().left().get()));
        }
        else {
            MissingNBTBlame.storeCurrentIdentifiers(null);
        }
    }
}
