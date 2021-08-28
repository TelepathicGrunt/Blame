package com.telepathicgrunt.blame.mixin;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.Lifecycle;
import com.telepathicgrunt.blame.main.WorldSettingsImportBlame;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldSettingsImport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;
import java.util.function.Supplier;

/* @author - TelepathicGrunt
 *
 * A mixin to make Minecraft actually tell me which
 * datapack json file broke. SPEAK TO ME MINECRAFT!
 *
 * LGPLv3
 */
@Mixin(WorldSettingsImport.IResourceAccess.RegistryAccess.class)
public class WorldSettingsImportInnerClassMixin {

    @Inject(method = "add(Lnet/minecraft/util/registry/DynamicRegistries$Impl;Lnet/minecraft/util/RegistryKey;Lcom/mojang/serialization/Encoder;ILjava/lang/Object;Lcom/mojang/serialization/Lifecycle;)V",
            at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V", remap = false),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private <E> void blame_addBrokenFileDetails(DynamicRegistries.Impl dynamicRegistries, RegistryKey<E> registryKey, Encoder<E> encoder, int p_244352_4_, E entry, Lifecycle lifecycle, CallbackInfo ci, DataResult<JsonElement> dataResult, Optional<DataResult.PartialResult<JsonElement>> error) {
        WorldSettingsImportBlame.printBrokenWorldgenElement(registryKey, entry, dataResult, error);
    }
}
