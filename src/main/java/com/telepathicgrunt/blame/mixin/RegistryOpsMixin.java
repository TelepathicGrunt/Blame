package com.telepathicgrunt.blame.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.telepathicgrunt.blame.Blame;
import com.telepathicgrunt.blame.utils.ErrorHints;
import com.telepathicgrunt.blame.utils.PrettyPrintBrokenJSON;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.BuiltinBiomes;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
@Mixin(RegistryOps.class)
public class RegistryOpsMixin<E> {

	@Unique
	private static Identifier currentResource;

	/**
	 * Grabs the current file we are at to pass to next mixin in case file explodes.
	 */
	@Inject(method = "loadToRegistry(Lnet/minecraft/util/registry/SimpleRegistry;Lnet/minecraft/util/registry/RegistryKey;Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/DataResult;",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;getPath()Ljava/lang/String;", ordinal = 1),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void getCurrentFile(SimpleRegistry<E> registry, RegistryKey<? extends Registry<E>> registryKey, Codec<E> codec,
								CallbackInfoReturnable<DataResult<SimpleRegistry<E>>> cir, Collection<Identifier> collection,
								DataResult<SimpleRegistry<E>> dataresult, String parentPath, Iterator<Identifier> identifierIterator,
								Identifier identifier)
	{
		currentResource = identifier;
	}

	/**
	 * Checks if the loaded datapack file errored and print it's resource location if it did
	 */
	@Inject(method = "loadToRegistry(Lnet/minecraft/util/registry/SimpleRegistry;Lnet/minecraft/util/registry/RegistryKey;Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/DataResult;",
			at = @At(value = "INVOKE_ASSIGN", target = "Lcom/mojang/serialization/DataResult;flatMap(Ljava/util/function/Function;)Lcom/mojang/serialization/DataResult;"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void addBrokenFileDetails(SimpleRegistry<E> registry, RegistryKey<? extends Registry<E>> registryKey, Codec<E> codec,
									  CallbackInfoReturnable<DataResult<SimpleRegistry<E>>> cir, Collection<Identifier> collection,
									  DataResult<SimpleRegistry<E>> dataresult)
	{
		if(dataresult.error().isPresent()){
			String brokenJSON = null;
			String reason = null;

			// Attempt to pull the JSON out of the error message if it exists.
			// Has a try/catch in case there's an error message that somehow breaks the string split.
			if(dataresult.error().isPresent()){
				try{
					String[] parsed = dataresult.error().get().message().split(": \\{", 2);
					reason = parsed[0];
					brokenJSON = "{" + parsed[1];
				}
				catch(Exception e){
					brokenJSON = "Failed to turn error msg into string. Please notify " +
							"TelepathicGrunt (Blame creator) and show him this message:  \n" + dataresult.error().get().message();
				}
			}

			Blame.LOGGER.log(Level.ERROR,
					"\n****************** Blame Report ******************"
					+ "\n\n Failed to load resource file: "+currentResource
					+ "\n\n Reason stated: " + reason
					+ "\n\n Possibly helpful hint (hopefully): " + ErrorHints.HINT_MAP.getOrDefault(reason, "If this is a worldgen JSON file, check out slicedlime's example datapack\n   for worldgen to find what's off about the JSON: https://t.co/cm3pJcAHcy?amp=1")
					+ "\n\n Prettified JSON: \n" + (brokenJSON != null ? PrettyPrintBrokenJSON.prettyPrintJSONAsString(brokenJSON) : " Unable to display JSON. ")
					+ "\n\n"
					);

		}
	}

}
