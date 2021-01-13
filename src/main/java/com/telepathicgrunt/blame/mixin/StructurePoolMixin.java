package com.telepathicgrunt.blame.mixin;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.blame.main.MissingNBTBlame;
import com.telepathicgrunt.blame.main.MissingTemplatePoolBlame;
import com.telepathicgrunt.blame.main.StructurePoolBlame;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* @author - TelepathicGrunt
 *
 *
 * isEmptyPool - Detect and print the empty template pool that is gonna crash game.
 *
 * tempPool and tempPoolClear - Make it so TemplateManager actually states what nbt file was unable to be found.
 *
 * LGPLv3
 */
@Mixin(StructurePool.class)
public abstract class StructurePoolMixin {

	@Redirect(method = "<init>(Lnet/minecraft/util/Identifier;Lnet/minecraft/util/Identifier;Ljava/util/List;)V",
			at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Lists;newArrayList()Ljava/util/ArrayList;"))
	private ArrayList<StructurePoolElement> tooLargePool(Identifier name, Identifier fallback,
												List<Pair<StructurePoolElement, Integer>> pieceElements)
	{
		for(Pair<StructurePoolElement, Integer> element : pieceElements){
			if(element.getSecond() > 100000){
				StructurePoolBlame.printExcessiveWeight(name, element);
			}
		}

		return new ArrayList<>();
	}

	@Final
	@Shadow
	private List<StructurePoolElement> elements;

	@Final
	@Shadow
	private Identifier id;

	// Detect and print the empty template pool that is gonna crash game.
	@Inject(method = "getRandomElement(Ljava/util/Random;)Lnet/minecraft/structure/pool/StructurePoolElement;",
			at = @At(value = "HEAD"))
	private void isEmptyPool(Random random, CallbackInfoReturnable<StructurePoolElement> cir)
	{
		if(elements.size() == 0){
			MissingTemplatePoolBlame.addEmptyPoolDetails(id, null);
		}
	}

	// Make it so TemplateManager actually states what nbt file was unable to be found.
	@Inject(method = "getHighestY(Lnet/minecraft/structure/StructureManager;)I",
			at = @At(value = "HEAD"))
	private void tempPool(StructureManager structureManager, CallbackInfoReturnable<Integer> cir)
	{
		MissingNBTBlame.CALLING_POOL = ((StructurePool)(Object)this).getId();
	}

	// Make it so TemplateManager actually states what nbt file was unable to be found.
	@Inject(method = "getHighestY(Lnet/minecraft/structure/StructureManager;)I",
			at = @At(value = "TAIL"))
	private void tempPoolClear(StructureManager structureManager, CallbackInfoReturnable<Integer> cir)
	{
		MissingNBTBlame.CALLING_POOL = null;
	}

}
