package com.telepathicgrunt.blame.mixin;

import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FluidTags.class)
public interface FluidTagsAccessor {
    @Accessor("HELPER")
    static TagRegistry<Fluid> getHELPER() {
        throw new UnsupportedOperationException();
    }
}
