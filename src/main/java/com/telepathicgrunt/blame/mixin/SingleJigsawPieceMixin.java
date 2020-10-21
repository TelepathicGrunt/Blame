package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.MissingNBTBlame;
import javafx.util.Pair;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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

	@Inject(method = "func_236843_a_(Lnet/minecraft/world/gen/feature/template/TemplateManager;)Lnet/minecraft/world/gen/feature/template/Template;",
			at = @At(value = "HEAD"))
	private void storeCurrentPool(TemplateManager templateManagerIn, CallbackInfoReturnable<MutableBoundingBox> cir)
	{
		if(MissingNBTBlame.CALLING_POOL != null &((SingleJigsawPieceAccessor)this).getTemplateRL().left().isPresent()){
			MissingNBTBlame.storeCurrentIdentifiers(new Pair<>(MissingNBTBlame.CALLING_POOL, ((SingleJigsawPieceAccessor)this).getTemplateRL().left().get()));
		}
		else{
			MissingNBTBlame.storeCurrentIdentifiers(null);
		}
	}
}
