package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.MissingNBTBlame;
import javafx.util.Pair;
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
@Mixin(JigsawPattern.class)
public class JigsawPatternMixin {

	@Unique
	private static JigsawPattern CALLING_POOL = null;

	@Inject(method = "getMaxSize(Lnet/minecraft/world/gen/feature/template/TemplateManager;)I",
			at = @At(value = "HEAD"))
	private void tempPool(TemplateManager templateManager, CallbackInfoReturnable<Integer> cir)
	{
		CALLING_POOL = (JigsawPattern)(Object)this;
	}

	@Dynamic
	@Inject(method = "*(Lnet/minecraft/world/gen/feature/template/TemplateManager;Lnet/minecraft/world/gen/feature/jigsaw/JigsawPiece;)I",
			at = @At(value = "HEAD"))
	private static void storeCurrentPool(TemplateManager templateManager, JigsawPiece jigsawPiece, CallbackInfoReturnable<Integer> cir)
	{
		if(jigsawPiece instanceof SingleJigsawPiece && ((SingleJigsawPieceAccessor)jigsawPiece).getTemplateRL().left().isPresent()){
			MissingNBTBlame.storeCurrentIdentifiers(new Pair<>(CALLING_POOL.getName(), ((SingleJigsawPieceAccessor)jigsawPiece).getTemplateRL().left().get()));
		}
		else{
			MissingNBTBlame.storeCurrentIdentifiers(null);
		}
	}
}
