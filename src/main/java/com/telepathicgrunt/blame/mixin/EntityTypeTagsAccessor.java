package com.telepathicgrunt.blame.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityTypeTags.class)
public interface EntityTypeTagsAccessor {
    @Accessor("HELPER")
    static TagRegistry<EntityType<?>> getHELPER() {
        throw new UnsupportedOperationException();
    }
}
