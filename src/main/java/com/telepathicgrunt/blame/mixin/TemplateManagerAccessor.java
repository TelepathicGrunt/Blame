package com.telepathicgrunt.blame.mixin;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

/* @author - TelepathicGrunt
 *
 * LGPLv3
 */
@Mixin(TemplateManager.class)
public interface TemplateManagerAccessor {

    @Accessor("structureRepository")
    Map<ResourceLocation, Template> blame_getStructureRepository();
}
