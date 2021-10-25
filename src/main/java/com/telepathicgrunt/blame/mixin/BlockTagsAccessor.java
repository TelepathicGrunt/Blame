package com.telepathicgrunt.blame.mixin;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockTags.class)
public interface BlockTagsAccessor {
    @Accessor("HELPER")
    static TagRegistry<Block> getHELPER() {
        throw new UnsupportedOperationException();
    }
}
