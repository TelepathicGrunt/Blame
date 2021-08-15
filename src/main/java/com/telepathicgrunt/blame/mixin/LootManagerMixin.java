package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.Blame;
import com.telepathicgrunt.blame.main.MissingLoottableBlame;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
    private void blame_addMissingLoottableDetails(Identifier ressources, CallbackInfoReturnable<LootTable> cir) {
        if (!tables.containsKey(ressources)) {
            MissingLoottableBlame.addMissingLoottableDetails(ressources);
        }
    }

    /**
     * Log a more useful message. Full stack trace is not useful. Concise, readable errors are useful.
     */
    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(method = "*(Lnet/minecraft/resources/IResourceManager;Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/util/ResourceLocation;Lcom/google/gson/JsonElement;)V", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"), require = 0, remap = false)
    private void blame_simplifyInvalidLootTableLogOutput(Logger logger, String message, Object p0, Object p1) {
        logger.error(message + " {}: {} (Blame {}: suppressed long stacktrace)", p0, p1.getClass().getSimpleName(), ((Exception) p1).getMessage(), Blame.VERSION);
    }
}
