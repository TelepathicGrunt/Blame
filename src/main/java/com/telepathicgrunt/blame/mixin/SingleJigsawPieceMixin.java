package com.telepathicgrunt.blame.mixin;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.blame.main.MissingNBTBlame;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* @author - TelepathicGrunt
 *
 * Make it so StructureManager actually states what nbt file was unable to be found.
 *
 * LGPLv3
 */
@Mixin(SingleJigsawPiece.class)
public class SingleJigsawPieceMixin {

	@Inject(method = "getTemplate(Lnet/minecraft/world/gen/feature/template/TemplateManager;)Lnet/minecraft/world/gen/feature/template/Template;",
			at = @At(value = "HEAD"))
	private void storeCurrentPool(TemplateManager templateManagerIn, CallbackInfoReturnable<MutableBoundingBox> cir)
	{
		if(MissingNBTBlame.CALLING_POOL != null &((SingleJigsawPieceAccessor)this).blame_getTemplateRL().left().isPresent()){
			MissingNBTBlame.storeCurrentIdentifiers(new Pair<>(MissingNBTBlame.CALLING_POOL, ((SingleJigsawPieceAccessor)this).blame_getTemplateRL().left().get()));
		}
		else{
			MissingNBTBlame.storeCurrentIdentifiers(null);
		}
	}
}
