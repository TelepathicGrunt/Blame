package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.Blame;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Map;

/* @author - TelepathicGrunt
 *
 * Detects if a crash is about to happen in FlatChunkGeneratorConfig
 * due to a mod's feature not being setup right for superflat.
 *
 * LGPLv3
 */
@Mixin(FlatChunkGeneratorConfig.class)
public class FlatChunkGeneratorConfigMixin {

	@Shadow
	@Final
	private static Map<StructureFeature<?>, ConfiguredStructureFeature<?, ?>> STRUCTURE_TO_FEATURES;

	/**
	 * Will detect if someone added the structure spacing to the chunkgenerator without adding
	 * the structure to the FlatGenerationSettings's STRUCTURES map. Doing so will cause a crash.
	 * This mixin will say what structure it was that triggered it.
	 */
	@Inject(method = "method_28917()Lnet/minecraft/world/biome/Biome;",
			at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void addCrashDetails(CallbackInfoReturnable<Biome> cir, Biome biome, GenerationSettings generationSettings,
								 GenerationSettings.Builder biomegenerationsettings$builder, Iterator<?> var4,
								 Map.Entry<StructureFeature<?>, ConfiguredStructureFeature<?, ?>> structureEntry)
	{
		// This condition will cause a crash
		if(STRUCTURE_TO_FEATURES.get(structureEntry.getKey()) == null){

			Identifier rl = Registry.STRUCTURE_FEATURE.getId(structureEntry.getKey());
			String extraDetail = rl != null ? (" | " + rl.toString()) : "";

			// Add extra info to the log before crash.
			String errorReport = "\n****************** Blame Report ******************" +
					"\n\n A crash is most likely going to happen right after this report!" +
					"\n It seems " + structureEntry.getKey().getName() + extraDetail + " is the cause because it is not added " +
					"\n to the FlatGenerationSettings.STRUCTURES map. Please let the mod owner " +
					"\n of that structure know about this crash. That way they can add their structure " +
					"\n to that map since someone is trying to spawn it in a flat/custom dimension. \n\n";

			// Log it to the latest.log file as well.
			Blame.LOGGER.log(Level.ERROR, errorReport);
		}
	}
}