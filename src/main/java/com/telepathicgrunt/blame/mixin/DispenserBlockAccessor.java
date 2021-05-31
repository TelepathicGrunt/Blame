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

    @Accessor("DISPENSER_REGISTRY")
    static void blame_setDISPENSER_REGISTRY(Map<Item, IDispenseItemBehavior> map) {
        throw new UnsupportedOperationException();
    }

    @Accessor("DISPENSER_REGISTRY")
    static Map<Item, IDispenseItemBehavior> blame_getDISPENSER_REGISTRY() {
        throw new UnsupportedOperationException();
    }
}
