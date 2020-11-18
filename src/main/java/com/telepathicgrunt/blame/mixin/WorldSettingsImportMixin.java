package com.telepathicgrunt.blame.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.telepathicgrunt.blame.main.WorldSettingsImportBlame;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.util.registry.WorldSettingsImport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.Iterator;

/* @author - TelepathicGrunt
 *
 * A mixin to make Minecraft actually tell me which
 * datapack json file broke. SPEAK TO ME MINECRAFT!
 *
 * LGPLv3
 */
@Mixin(WorldSettingsImport.class)
public class WorldSettingsImportMixin<E> {

	/**
	 * Grabs the current file we are at to pass to next mixin in case file explodes.
	 */
	@Inject(method = "decode(Lnet/minecraft/util/registry/SimpleRegistry;Lnet/minecraft/util/RegistryKey;Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/DataResult;",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ResourceLocation;getPath()Ljava/lang/String;", ordinal = 1),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void getCurrentFile(SimpleRegistry<E> registry, RegistryKey<? extends Registry<E>> registryKey, Codec<E> codec,
								CallbackInfoReturnable<DataResult<SimpleRegistry<E>>> cir, Collection<ResourceLocation> collection,
								DataResult<SimpleRegistry<E>> dataresult, String parentPath, Iterator<ResourceLocation> resourceLocationIterator,
								ResourceLocation resourceLocation)
	{
		WorldSettingsImportBlame.getCurrentFile(resourceLocation);
	}

	/**
	 * Checks if the loaded datapack file errored and print it's resource location if it did
	 */
	@Inject(method = "decode(Lnet/minecraft/util/registry/SimpleRegistry;Lnet/minecraft/util/RegistryKey;Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/DataResult;",
			at = @At(value = "INVOKE_ASSIGN", target = "Lcom/mojang/serialization/DataResult;flatMap(Ljava/util/function/Function;)Lcom/mojang/serialization/DataResult;"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void addBrokenFileDetails(SimpleRegistry<E> registry, RegistryKey<? extends Registry<E>> registryKey, Codec<E> codec,
									  CallbackInfoReturnable<DataResult<SimpleRegistry<E>>> cir, Collection<ResourceLocation> collection,
									  DataResult<SimpleRegistry<E>> dataresult)
	{
		WorldSettingsImportBlame.addBrokenFileDetails(dataresult);
	}
}
