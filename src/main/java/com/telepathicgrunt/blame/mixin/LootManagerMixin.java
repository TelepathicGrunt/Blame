package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.MissingLoottableBlame;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;
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
@Mixin(LootManager.class)
public class LootManagerMixin {

	@Shadow
	private Map<Identifier, LootTable> tables;

	@Inject(method = "getTable(Lnet/minecraft/util/Identifier;)Lnet/minecraft/loot/LootTable;",
			at = @At(value = "HEAD"))
	private void addMissingLoottableDetails(Identifier ressources, CallbackInfoReturnable<LootTable> cir)
	{
		if(!tables.containsKey(ressources)){
			MissingLoottableBlame.addMissingLoottableDetails(ressources);
		}
	}
}
