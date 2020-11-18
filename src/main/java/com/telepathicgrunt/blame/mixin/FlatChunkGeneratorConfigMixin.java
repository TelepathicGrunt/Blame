package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.FlatChunkGeneratorConfigBlame;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.StructureFeature;
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
	 * the structure to the FlatChunkGeneratorConfig's STRUCTURE_FEATURE map. Doing so will cause a crash.
	 * This mixin will say what structure it was that triggered it.
	 */
	@Inject(method = "createBiome()Lnet/minecraft/world/biome/Biome;",
			at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void addCrashDetails(CallbackInfoReturnable<Biome> cir, Biome biome, GenerationSettings generationSettings,
								 GenerationSettings.Builder biomegenerationsettings$builder, Iterator<?> var4,
								 Map.Entry<StructureFeature<?>, ConfiguredStructureFeature<?, ?>> structureEntry)
	{
		FlatChunkGeneratorConfigBlame.addCrashDetails(STRUCTURE_TO_FEATURES, structureEntry);
	}
}
