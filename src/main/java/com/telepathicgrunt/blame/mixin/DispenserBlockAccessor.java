package com.telepathicgrunt.blame.mixin;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

/* @author - TelepathicGrunt
 *
 * LGPLv3
 */
@Mixin(DispenserBlock.class)
public interface DispenserBlockAccessor {

    @Mutable
    @Accessor("BEHAVIORS")
    static void blame_setBEHAVIORS(Map<Item, DispenserBehavior> map) {
        throw new UnsupportedOperationException();
    }

    @Accessor("BEHAVIORS")
    static Map<Item, DispenserBehavior> blame_getBEHAVIORS() {
        throw new UnsupportedOperationException();
    }
}
