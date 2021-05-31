package com.telepathicgrunt.blame.mixin;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.blame.main.JigsawPatternBlame;
import com.telepathicgrunt.blame.main.MissingNBTBlame;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/* @author - TelepathicGrunt
 *
 * Make it so StructureManager actually states what nbt file was unable to be found.
 *
 * LGPLv3
 */
@Mixin(JigsawPattern.class)
public class JigsawPatternMixin {

    @Redirect(method = "<init>(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/util/ResourceLocation;Ljava/util/List;)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/datafixers/util/Pair;getFirst()Ljava/lang/Object;"))
    private <F> F tooLargePool(Pair<F, Integer> pair, ResourceLocation name, ResourceLocation fallback, List<Pair<JigsawPiece, Integer>> pieceElements) {
        if (pair.getSecond() > 100000) {
            JigsawPatternBlame.printExcessiveWeight(name, (Pair<JigsawPiece, Integer>) pair);
        }

        return pair.getFirst();
    }

    @Inject(method = "getMaxSize(Lnet/minecraft/world/gen/feature/template/TemplateManager;)I",
            at = @At(value = "HEAD"))
    private void tempPool(TemplateManager templateManager, CallbackInfoReturnable<Integer> cir) {
        MissingNBTBlame.CALLING_POOL = ((JigsawPattern) (Object) this).getName();
    }

    @Inject(method = "getMaxSize(Lnet/minecraft/world/gen/feature/template/TemplateManager;)I",
            at = @At(value = "TAIL"))
    private void tempPoolClear(TemplateManager templateManager, CallbackInfoReturnable<Integer> cir) {
        MissingNBTBlame.CALLING_POOL = null;
    }
}
