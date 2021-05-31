package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.ProcessorBlame;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
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
@Mixin(Template.class)
public class TemplateMixin {

    @Redirect(method = "processBlockInfos(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/template/PlacementSettings;Ljava/util/List;Lnet/minecraft/world/gen/feature/template/Template;)Ljava/util/List;",
            remap = false,
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/template/StructureProcessor;process(Lnet/minecraft/world/IWorldReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/template/Template$BlockInfo;Lnet/minecraft/world/gen/feature/template/Template$BlockInfo;Lnet/minecraft/world/gen/feature/template/PlacementSettings;Lnet/minecraft/world/gen/feature/template/Template;)Lnet/minecraft/world/gen/feature/template/Template$BlockInfo;"))
    private static Template.BlockInfo processBlockInfos(StructureProcessor structureProcessor, IWorldReader world, BlockPos blockPos1, BlockPos blockPos2, Template.BlockInfo blockInfo1, Template.BlockInfo blockInfo2, PlacementSettings placementSettings, Template template) {
        return ProcessorBlame.findBrokenProcessor(structureProcessor, world, blockPos1, blockPos2, blockInfo1, blockInfo2, placementSettings, template);
    }
}
