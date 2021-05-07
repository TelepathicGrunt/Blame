package com.telepathicgrunt.blame.mixin;

import com.mojang.datafixers.util.Either;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

/* @author - TelepathicGrunt
 *
 * LGPLv3
 */
@Mixin(TemplateManager.class)
public interface TemplateManagerAccessor {

	@Accessor("templates")
	Map<ResourceLocation, Template> blame_gettemplates();
}
