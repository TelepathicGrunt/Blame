package com.telepathicgrunt.blame.mixin;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(Structure.class)
public interface StructureAccessor {
    @Accessor("STEP")
    static Map<Structure<?>, GenerationStage.Decoration> blame_getSTEP() {
        throw new UnsupportedOperationException();
    }
}
