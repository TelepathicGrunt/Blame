package com.telepathicgrunt.blame.mixin;

import com.mojang.datafixers.util.Either;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.template.Template;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/* @author - TelepathicGrunt
 *
 * Make it so TemplateManager actually states what nbt file was unable to be found.
 *
 * LGPLv3
 */
@Mixin(SingleJigsawPiece.class)
public interface SingleJigsawPieceAccessor {

	@Accessor("field_236839_c_")
	Either<ResourceLocation, Template> getTemplateRL();
}
