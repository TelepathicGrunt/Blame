package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.BiomeBlame;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

/* @author - TelepathicGrunt
 *
 * Two small mixins to make crashes during feature gen and structure gen now
 * output info about the feature, structure, and biome into the crashlog and
 * into the latest.log. Basically it needs more info as it is impossible
 * to find the broken feature before.
 *
 * LGPLv3
 */
@Mixin(Biome.class)
public class BiomeMixin {
    /**
     * Place blame on broke feature during worldgen.
     * Prints registry name of feature and biome.
     * Prints the crashlog to latest.log as well.
     */
    @Inject(method = "generateFeatureStep(Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/ChunkRegion;JLnet/minecraft/world/gen/ChunkRandom;Lnet/minecraft/util/math/BlockPos;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;", ordinal = 1),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void addFeatureDetails(StructureAccessor structureAccessor, ChunkGenerator chunkGenerator,
                                   ChunkRegion chunkRegion, long seed, ChunkRandom random, BlockPos pos,
                                   CallbackInfo ci, List<List<Supplier<ConfiguredFeature<?, ?>>>> list,
                                   Registry<ConfiguredFeature<?, ?>> registry, Registry<StructureFeature<?>> structureFeatures,
                                   int numOfGenerationSteps, int generationStepIndex, int configuredFeatureIndex,
                                   Iterator<ConfiguredFeature<?, ?>> var12, Supplier<ConfiguredFeature<?, ?>> supplier,
                                   ConfiguredFeature<?, ?> configuredFeature, Supplier<String> supplier3,
                                   Exception exception) {
        BiomeBlame.addFeatureDetails((Biome) (Object) this, chunkRegion, configuredFeature);
    }


    /**
     * Place blame on broke structures during worldgen.
     * Prints registry name of structure and biome.
     * Prints the crashlog to latest.log as well.
     */
    @Inject(method = "generateFeatureStep(Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/ChunkRegion;JLnet/minecraft/world/gen/ChunkRandom;Lnet/minecraft/util/math/BlockPos;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/crash/CrashReport;create(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/util/crash/CrashReport;", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void addStructureDetails(StructureAccessor structureAccessor, ChunkGenerator chunkGenerator,
                                     ChunkRegion chunkRegion, long seed, ChunkRandom random, BlockPos pos,
                                     CallbackInfo ci, List<List<Supplier<ConfiguredFeature<?, ?>>>> list,
                                     Registry<ConfiguredFeature<?, ?>> registry, Registry<StructureFeature<?>> structureFeatures,
                                     int numOfGenerationSteps, int generationStepIndex, int configuredFeatureIndex,
                                     List<List<Supplier<ConfiguredFeature<?, ?>>>> list2,
                                     Iterator<StructureFeature<?>> structureFeatureIterator,
                                     StructureFeature<?> structureFeature,
                                     int sectionCordX, int sectionCordZ, int blockCordX, int blockCordZ,
                                     Supplier<String> supplier, Exception exception) {
        BiomeBlame.addStructureDetails((Biome) (Object) this, chunkRegion, structureFeature);
    }
}
