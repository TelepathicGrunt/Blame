package com.telepathicgrunt.blame.mixin;

import com.mojang.datafixers.util.Either;
import net.minecraft.structure.Structure;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Supplier;

/* @author - TelepathicGrunt
 *
 * Make it so StructureManager actually states what nbt file was unable to be found.
 *
 * LGPLv3
 */
@Mixin(SinglePoolElement.class)
public interface SinglePoolElementAccessor {

    @Accessor("location")
    Either<Identifier, Structure> blame_getTemplateID();

    @Accessor("processors")
    Supplier<StructureProcessorList> blame_getProcessors();
}
