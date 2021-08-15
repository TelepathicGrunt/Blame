package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.DispenserBlockRegistry;
import net.minecraft.Bootstrap;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.Item;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* @author - TelepathicGrunt
 *
 * Allows us to find which mod is overriding dispenser behaviors
 *
 * LGPLv3
 */
@Mixin(Bootstrap.class)
public class BootstrapMixin {

    @Inject(method = "initialize()V",
            at = @At(value = "TAIL"))
    private static void blame_onInit(CallbackInfo ci) {
        DispenserBlockRegistry<Item, DispenserBehavior> map = Util.make(new DispenserBlockRegistry<>(), (behaviour) -> behaviour.defaultReturnValue(new ItemDispenserBehavior()));
        map.putAll(DispenserBlockAccessor.blame_getBEHAVIORS());
        map.startupIgnore = false; // Finished copying. Now turn on registry replacement detection.
        DispenserBlockAccessor.blame_setBEHAVIORS(map);
    }
}
