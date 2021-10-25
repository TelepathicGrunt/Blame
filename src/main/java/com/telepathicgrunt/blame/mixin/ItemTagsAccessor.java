package com.telepathicgrunt.blame.mixin;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemTags.class)
public interface ItemTagsAccessor {
    @Accessor("HELPER")
    static TagRegistry<Item> getHELPER() {
        throw new UnsupportedOperationException();
    }
}
