package com.telepathicgrunt.blame.mixin;

import java.util.Map;

import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.util.ResourceLocation;

import com.telepathicgrunt.blame.main.MissingLoottableBlame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* @author - TelepathicGrunt
 *
 * Will print out any loottable that doesn't exist.
 *
 * LGPLv3
 */
@Mixin(LootTableManager.class)
public abstract class LootTableManagerMixin extends JsonReloadListener
{
	private LootTableManagerMixin(Gson gson_, String string_)
	{
		super(gson_, string_);
	}

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

	/**
	 * Log a more useful message. Full stack trace is not useful. Concise, readable errors are useful.
	 */
	@SuppressWarnings("UnresolvedMixinReference")
	@Redirect(method = "*(Lnet/minecraft/resources/IResourceManager;Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/util/ResourceLocation;Lcom/google/gson/JsonElement;)V", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"), require = 0, remap = false)
	private void simplifyInvalidLootTableLogOutput(Logger logger, String message, Object p0, Object p1)
	{
		logger.error(message + " {}: {} (Blame: suppressed long stacktrace)", p0, p1.getClass().getSimpleName(), ((Exception) p1).getMessage());
	}
}
