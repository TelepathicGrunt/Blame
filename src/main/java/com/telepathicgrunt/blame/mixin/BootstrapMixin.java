package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.DispenserBlockRegistry;
import net.minecraft.Bootstrap;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.Item;
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
	private static void onInit(CallbackInfo ci)
	{
		DispenserBlockRegistry<Item, DispenserBehavior>  map = new DispenserBlockRegistry<>();
		map.putAll(DispenserBlockAccessor.getBEHAVIORS());
		DispenserBlockAccessor.setBEHAVIORS(map);
	}
}
