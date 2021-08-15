package com.telepathicgrunt.blame.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.telepathicgrunt.blame.main.WorldSettingsImportBlame;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldSettingsImport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

/* @author - TelepathicGrunt
 *
 * A mixin to make Minecraft actually tell me which
 * datapack json file broke. SPEAK TO ME MINECRAFT!
 *
 * LGPLv3
 */
@Mixin(WorldSettingsImport.class)
public class WorldSettingsImportMixin<E> {
    @Inject(method = "readAndRegisterElement", at = @At(value = "RETURN"))
    private void blame_addBrokenFileDetails(RegistryKey<? extends Registry<E>> registryKey, MutableRegistry<E> mutableRegistry, Codec<E> mapCodec, ResourceLocation id, CallbackInfoReturnable<DataResult<Supplier<E>>> cir) {
        WorldSettingsImportBlame.addBrokenFileDetails(registryKey, id, cir.getReturnValue());
    }
}
