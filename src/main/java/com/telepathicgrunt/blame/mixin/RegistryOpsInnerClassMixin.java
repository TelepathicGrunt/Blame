package com.telepathicgrunt.blame.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.telepathicgrunt.blame.Blame;
import com.telepathicgrunt.blame.utils.ErrorHints;
import com.telepathicgrunt.blame.utils.PrettyPrintBrokenJSON;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;
import java.util.OptionalInt;

/* @author - TelepathicGrunt
 *
 *
 *
 * LGPLv3
 */
@Mixin(targets = "net/minecraft/util/dynamic/RegistryOps$EntryLoader$1")
public class RegistryOpsInnerClassMixin<E> {
//
//	@Inject(method = "load(Lcom/mojang/serialization/DynamicOps;Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/util/registry/RegistryKey;Lcom/mojang/serialization/Decoder;)Lcom/mojang/serialization/DataResult;",
//			at = @At(value = "INVOKE_ASSIGN", target = "Lcom/mojang/serialization/Decoder;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"),
//			locals = LocalCapture.CAPTURE_FAILHARD)
//	private void addBrokenFileDetails(DynamicOps<JsonElement> arg0, RegistryKey<? extends Registry<E>> arg1,
//									  RegistryKey<E> entryId, Decoder<E> arg3, CallbackInfoReturnable<DataResult<Pair<E, OptionalInt>>> cir,
//									  Identifier identifier, Identifier identifier2, Resource resource, @Coerce Object var8, Reader reader,
//									  @Coerce Object var10, JsonParser jsonParser, JsonElement jsonElement)
//	{
////		if(registryKey2.getValue().toString().contains("fake")){
////			int t = 5;
////		}
////		if(dataresult.error().isPresent()){
////			String brokenJSON = null;
////			String reason = null;
////
////			// Attempt to pull the JSON out of the error message if it exists.
////			// Has a try/catch in case there's an error message that somehow breaks the string split.
////			if(dataresult.error().isPresent()){
////				try{
////					String[] parsed = dataresult.error().get().message().split(": \\{", 2);
////					reason = parsed[0];
////					brokenJSON = "{" + parsed[1];
////				}
////				catch(Exception e){
////					brokenJSON = "Failed to turn error msg into string. Please notify " +
////							"TelepathicGrunt (Blame creator) and show him this message:  \n" + dataresult.error().get().message();
////				}
////			}
////
////			Blame.LOGGER.log(Level.ERROR,
////					"\n****************** Blame Report ******************"
////					+ "\n\n Failed to load resource file: "+currentResource
////					+ "\n\n Reason stated: " + reason
////					+ "\n\n Possibly helpful hint (hopefully): " + ErrorHints.HINT_MAP.getOrDefault(reason, "If this is a worldgen JSON file, check out slicedlime's example datapack\n   for worldgen to find what's off about the JSON: https://t.co/cm3pJcAHcy?amp=1")
////					+ "\n\n Prettified JSON: \n" + (brokenJSON != null ? PrettyPrintBrokenJSON.prettyPrintJSONAsString(brokenJSON) : " Unable to display JSON. ")
////					+ "\n\n"
////					);
////
////		}
//	}

}
