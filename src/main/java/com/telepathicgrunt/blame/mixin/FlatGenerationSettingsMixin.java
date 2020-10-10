package com.telepathicgrunt.blame.mixin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

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
		// This condition will cause a crash
		if(STRUCTURES.get(structureEntry.getKey()) == null){

			ResourceLocation rl = Registry.STRUCTURE_FEATURE.getKey(structureEntry.getKey());
			String extraDetail = rl != null ? (" | " + rl.toString()) : "";

			// Add extra info to the log before crash.
			String errorReport = "\n****************** Blame Report ******************" +
					"\n\n A crash is most likely going to happen right after this report!" +
					"\n It seems " + structureEntry.getKey().getStructureName() + extraDetail + " is the cause because it is not added " +
					"\n to the FlatGenerationSettings.STRUCTURES map. Please let the mod owner " +
					"\n of that structure know about this crash. That way they can add their structure " +
					"\n to that map since someone is trying to spawn it in a flat/custom dimension. \n\n";

			// Log it to the latest.log file as well.
			Blame.LOGGER.log(Level.ERROR, errorReport);
		}
	}
}
