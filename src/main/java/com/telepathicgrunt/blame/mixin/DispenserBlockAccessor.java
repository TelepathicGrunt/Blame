package com.telepathicgrunt.blame.mixin;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

/* @author - TelepathicGrunt
 *
 * LGPLv3
 */
@Mixin(DispenserBlock.class)
public interface DispenserBlockAccessor {

	@Accessor("DISPENSE_BEHAVIOR_REGISTRY")
	static void blame_setDISPENSE_BEHAVIOR_REGISTRY(Map<Item, IDispenseItemBehavior> map) {
		throw new UnsupportedOperationException();
	}

	@Accessor("DISPENSE_BEHAVIOR_REGISTRY")
	static Map<Item, IDispenseItemBehavior> blame_getDISPENSE_BEHAVIOR_REGISTRY() {
		throw new UnsupportedOperationException();
	}
}
