package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.DispenserBlockRegistry;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.item.Item;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Bootstrap;
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

	@Inject(method = "register()V",
			at = @At(value = "TAIL"))
	private static void onInit(CallbackInfo ci)
	{
		DispenserBlockRegistry<Item, IDispenseItemBehavior> map = Util.make(new DispenserBlockRegistry<>(), (behaviour) -> behaviour.defaultReturnValue(new DefaultDispenseItemBehavior()));
		map.putAll(DispenserBlockAccessor.getDISPENSE_BEHAVIOR_REGISTRY());
		map.startupIgnore = false; // Finished copying. Now turn on registry replacement detection.
		DispenserBlockAccessor.setDISPENSE_BEHAVIOR_REGISTRY(map);
	}
}
