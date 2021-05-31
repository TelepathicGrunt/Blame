package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.ProcessorBlame;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/* @author - TelepathicGrunt
 *
 * Find why an nbt file is exploding when loading blocks from the files or why a processor is blowing up.
 * Can be quite useful to find which Jigsaw Block's replacement block field is mispelled.
 *
 * LGPLv3
 */
@Mixin(Structure.class)
public class StructureMixin {

    @Redirect(method = "process(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/StructurePlacementData;Ljava/util/List;)Ljava/util/List;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/structure/processor/StructureProcessor;process(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/Structure$StructureBlockInfo;Lnet/minecraft/structure/Structure$StructureBlockInfo;Lnet/minecraft/structure/StructurePlacementData;)Lnet/minecraft/structure/Structure$StructureBlockInfo;"))
    private static Structure.StructureBlockInfo processBlockInfos(StructureProcessor structureProcessor, WorldView world, BlockPos blockPos1, BlockPos blockPos2, Structure.StructureBlockInfo blockInfo1, Structure.StructureBlockInfo blockInfo2, StructurePlacementData placementSettings) {
        return ProcessorBlame.findBrokenProcessor(structureProcessor, world, blockPos1, blockPos2, blockInfo1, blockInfo2, placementSettings);
    }
}
