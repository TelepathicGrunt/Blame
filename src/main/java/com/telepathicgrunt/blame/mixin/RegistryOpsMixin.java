package com.telepathicgrunt.blame.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.telepathicgrunt.blame.main.RegistryOpsBlame;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

/* @author - TelepathicGrunt
 *
 * A mixin to make Minecraft actually tell me which
 * datapack json file broke. SPEAK TO ME MINECRAFT!
 *
 * LGPLv3
 */
@Mixin(RegistryOps.class)
public class RegistryOpsMixin<E> {

    @Inject(method = "readSupplier", at = @At(value = "RETURN"))
    private void addBrokenFileDetails(RegistryKey<? extends Registry<E>> registryKey, MutableRegistry<E> mutableRegistry, Codec<E> mapCodec, Identifier id, CallbackInfoReturnable<DataResult<Supplier<E>>> cir) {
        RegistryOpsBlame.addBrokenFileDetails(registryKey, id, cir.getReturnValue());
    }
}
