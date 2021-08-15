package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.Blame;
import com.telepathicgrunt.blame.main.MissingLoottableBlame;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DatagenModLoader;
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
@Mixin(LootTableManager.class)
public class LootTableManagerMixin {

    @Shadow
    private Map<ResourceLocation, LootTable> tables;

    @Inject(method = "get(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/loot/LootTable;",
            at = @At(value = "HEAD"))
    private void blame_addMissingLoottableDetails(ResourceLocation rl, CallbackInfoReturnable<LootTable> cir) {
        if (!tables.containsKey(rl)) {
            if(DatagenModLoader.isRunningDataGen()) return;
            MissingLoottableBlame.addMissingLoottableDetails(rl);
        }
    }

    /**
     * Log a more useful message. Full stack trace is not useful. Concise, readable errors are useful.
     */
    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(method = "*(Lnet/minecraft/resources/IResourceManager;Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/util/ResourceLocation;Lcom/google/gson/JsonElement;)V",
            at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false), require = 0)
    private void blame_simplifyInvalidLootTableLogOutput(Logger logger, String message, Object p0, Object p1) {
        if(DatagenModLoader.isRunningDataGen()) return;
        logger.error(message + " {}: {} (Blame {}: suppressed long stacktrace)", p0, p1.getClass().getSimpleName(), ((Exception) p1).getMessage(), Blame.VERSION);
    }
}
