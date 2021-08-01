package com.telepathicgrunt.blame.mixin;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.Lifecycle;
import com.telepathicgrunt.blame.main.RegistryOpsBlame;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

/* @author - TelepathicGrunt
 *
 * Prints more info about the broken Worldgen element that failed to be parsed.
 *
 * LGPLv3
 */
@Mixin(RegistryOps.EntryLoader.Impl.class)
public class RegistryOpsInnerClassMixin {

    @Inject(method = "add(Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;Lnet/minecraft/util/registry/RegistryKey;Lcom/mojang/serialization/Encoder;ILjava/lang/Object;Lcom/mojang/serialization/Lifecycle;)V",
            at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V", remap = false),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private <E> void printBrokenWorldgenElementInfo(DynamicRegistryManager.Impl registryManager, RegistryKey<E> key, Encoder<E> encoder, int rawId, E entry, Lifecycle lifecycle, CallbackInfo ci, DataResult<JsonElement> dataResult, Optional<DataResult.PartialResult<JsonElement>> error) {
        RegistryOpsBlame.printBrokenWorldgenElement(key, entry, dataResult, error);
    }
}
