package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.MissingLoottableBlame;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

/* @author - TelepathicGrunt
 *
 * Will print out any loottable that doesn't exist.
 *
 * LGPLv3
 */
@Mixin(LootTableManager.class)
public class LootTableManagerMixin {

	@Shadow
	private Map<ResourceLocation, LootTable> tables;

	@Inject(method = "get(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/loot/LootTable;",
			at = @At(value = "HEAD"))
	private void addMissingLoottableDetails(ResourceLocation rl, CallbackInfoReturnable<LootTable> cir)
	{
		if(!tables.containsKey(rl)){
			MissingLoottableBlame.addMissingLoottableDetails(rl);
		}
	}
}
