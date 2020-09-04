package com.telepathicgrunt.blame.mixin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.JsonOps;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Mixin(Biome.class)
public class BiomeMixin {
	/**
	 * Place blame on broke feature during worldgen.
	 * Prints registry name of feature and biome.
	 * Prints the crashlog to latest.log as well.
	 */
	@Inject(method = "generateFeatureStep(Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/ChunkRegion;JLnet/minecraft/world/gen/ChunkRandom;Lnet/minecraft/util/math/BlockPos;)V",
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;", ordinal = 1),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void addFeatureDetails(StructureAccessor structureAccessor, ChunkGenerator chunkGenerator,
								   ChunkRegion chunkRegion, long seed, ChunkRandom random, BlockPos pos,
								   CallbackInfo ci, List<List<Supplier<ConfiguredFeature<?, ?>>>> GenerationStageList,
								   int numOfGenerationStage, int generationStageIndex, int configuredFeatureIndex,
								   Iterator<ConfiguredFeature<?, ?>> var12, Supplier<ConfiguredFeature<?, ?>> supplier, ConfiguredFeature<?, ?> configuredfeature,
								   Exception exception, CrashReport crashreport)
	{
		DynamicRegistryManager dynamicRegistries = chunkRegion.getRegistryManager();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		crashreport.getSystemDetailsSection()
				.add("\n****************** Blame Report ******************",
					"\n\n ConfiguredFeature Registry Name : " + Objects.requireNonNull(dynamicRegistries.get(Registry.CONFIGURED_FEATURE_WORLDGEN).getKey(configuredfeature)) +
					"\n Biome Registry Name : " + Objects.requireNonNull(dynamicRegistries.get(Registry.BIOME_KEY).getKey((Biome)(Object)this)) +
					"\n\n JSON info : " +
							(ConfiguredFeature.CODEC.encode(() -> configuredfeature, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left().isPresent()
									? gson.toJson(ConfiguredFeature.CODEC.encode(() -> configuredfeature, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left().get())
									: " failed to get json.") + "\n\n");

		Blame.LOGGER.log(Level.ERROR, crashreport.getMessage());
	}


	/**
	 * Place blame on broke structures during worldgen.
	 * Prints registry name of feature and biome.
	 * Prints the crashlog to latest.log as well.
	 */
	@Inject(method = "generateFeatureStep(Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/ChunkRegion;JLnet/minecraft/world/gen/ChunkRandom;Lnet/minecraft/util/math/BlockPos;)V",
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;", ordinal = 0),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void addStructureDetails(StructureAccessor structureAccessor, ChunkGenerator chunkGenerator,
									 ChunkRegion chunkRegion, long seed, ChunkRandom random, BlockPos pos,
									 CallbackInfo ci, int numOfGenerationStage, int generationStageIndex,
									 int structureIndex, List<StructureFeature<?>> list, Iterator<StructureFeature<?>> var12,
									 StructureFeature<?> structureFeature, Exception exception, CrashReport crashreport)
	{
		DynamicRegistryManager dynamicRegistries = chunkRegion.getRegistryManager();

		crashreport.getSystemDetailsSection()
				.add("\n****************** Blame Report ******************",
				"\n\n Structure Name : " + structureFeature.getName() +
				"\n Structure Registry Name : " + Objects.requireNonNull(dynamicRegistries.get(Registry.STRUCTURE_FEATURE_KEY).getKey(structureFeature)) +
				"\n Biome Registry Name : " + Objects.requireNonNull(dynamicRegistries.get(Registry.BIOME_KEY).getKey((Biome)(Object)this)) + "\n\n");

		Blame.LOGGER.log(Level.ERROR, crashreport.getMessage());
	}
}
