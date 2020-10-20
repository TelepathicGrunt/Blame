package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.FlatGenerationSettingsBlame;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
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
 * Detects if a crash is about to happen in FlatGenerationSettings
 * due to a mod's feature not being setup right for superflat.
 *
 * LGPLv3
 */
@Mixin(FlatGenerationSettings.class)
public class FlatGenerationSettingsMixin {

	@Shadow
	@Final
	private static Map<Structure<?>, StructureFeature<?, ?>> STRUCTURES;

	/**
	 * Will detect if someone added the structure spacing to the chunkgenerator without adding
	 * the structure to the FlatGenerationSettings's STRUCTURES map. Doing so will cause a crash.
	 * This mixin will say what structure it was that triggered it.
	 */
	@Inject(method = "func_236942_c_()Lnet/minecraft/world/biome/Biome;",
			at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void addCrashDetails(CallbackInfoReturnable<Biome> cir, Biome biome, BiomeGenerationSettings biomegenerationsettings,
								 BiomeGenerationSettings.Builder biomegenerationsettings$builder, Iterator<?> var4,
								 Map.Entry<Structure<?>, StructureSeparationSettings> structureEntry)
	{
		FlatGenerationSettingsBlame.addCrashDetails(STRUCTURES, structureEntry);
	}
}
