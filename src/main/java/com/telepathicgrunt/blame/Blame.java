package com.telepathicgrunt.blame;

import com.telepathicgrunt.blame.mixin.GenerationSettingsAccessor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.AcaciaFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;
import net.minecraft.world.gen.trunk.ForkingTrunkPlacer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Blame implements ModInitializer {
    public static final String MODID = "blame";
    public static final Logger LOGGER = LogManager.getLogger();
    public static String VERSION = "N/A";

    @Override
    public void onInitialize() {
        FabricLoader.getInstance().getModContainer(MODID)
                .ifPresent(container -> VERSION = container.getMetadata().getVersion().toString());

        Blame.LOGGER.log(Level.ERROR, "Blame " + VERSION + " initialized");


//        Biome newBiome = (new Biome.Builder())
//                .precipitation(Biome.Precipitation.RAIN)
//                .category(Biome.Category.FOREST)
//                .depth(5)
//                .scale(5)
//                .temperature(0.6F)
//                .downfall(0.6F)
//                .effects((new BiomeEffects.Builder())
//                        .waterColor(9204)
//                        .waterFogColor(9011)
//                        .fogColor(8463)
//                        .skyColor(3534).build())
//                .spawnSettings(new SpawnSettings.Builder().build())
//                .generationSettings((new GenerationSettings.Builder())
//                        .surfaceBuilder(ConfiguredSurfaceBuilders.GRASS).build())
//                .build();
//        BuiltinRegistries.add(BuiltinRegistries.BIOME, new Identifier(Blame.MODID, "test_code_biome"), newBiome);


//        // Unregistered worldgen test
//        // Make the structure and features list mutable for modification later
//        List<List<Supplier<ConfiguredFeature<?, ?>>>> tempFeature = ((GenerationSettingsAccessor)BuiltinBiomes.PLAINS.getGenerationSettings()).getFeatures();
//        List<List<Supplier<ConfiguredFeature<?, ?>>>> mutableGenerationStages = new ArrayList<>();
//
//        // Fill in generation stages so there are at least 9 or else Minecraft crashes.
//        // (we need all stages for adding features/structures to the right stage too)
//        for(int currentStageIndex = 0; currentStageIndex < Math.max(10, tempFeature.size()); currentStageIndex++){
//            if(currentStageIndex >= tempFeature.size()){
//                mutableGenerationStages.add(new ArrayList<>());
//            }else{
//                mutableGenerationStages.add(new ArrayList<>(tempFeature.get(currentStageIndex)));
//            }
//        }
//
//        // Make the Structure and GenerationStages (features) list mutable for modification later
//        ((GenerationSettingsAccessor)BuiltinBiomes.PLAINS.getGenerationSettings()).setFeatures(mutableGenerationStages);
//
//        BuiltinBiomes.PLAINS.getGenerationSettings().getFeatures().get(GenerationStep.Feature.VEGETAL_DECORATION.ordinal()).add(() -> Feature.DESERT_WELL.configure(DefaultFeatureConfig.INSTANCE));

//        ConfiguredFeature<?,?> brokenTree =
//                Feature.TREE.configure(
//                        (new TreeFeatureConfig.Builder(
//                                new SimpleBlockStateProvider(Blocks.ACACIA_LOG.getDefaultState()),
//                                new ForkingTrunkPlacer(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE),
//                                new SimpleBlockStateProvider(Blocks.ACACIA_LEAVES.getDefaultState()),
//                                new SimpleBlockStateProvider(Blocks.ACACIA_SAPLING.getDefaultState()),
//                                new AcaciaFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0)),
//                                new TwoLayersFeatureSize(1, 0, 2)))
//                        .ignoreVines()
//                        .build());
//
//        // Make the structure and features list mutable for modification later
//        List<List<Supplier<ConfiguredFeature<?, ?>>>> tempFeature = ((GenerationSettingsAccessor) BuiltinBiomes.PLAINS.getGenerationSettings()).getFeatures();
//        List<List<Supplier<ConfiguredFeature<?, ?>>>> mutableGenerationStages = new ArrayList<>();
//
//        // Fill in generation stages so there are at least 9 or else Minecraft crashes.
//        // (we need all stages for adding features/structures to the right stage too)
//        for(int currentStageIndex = 0; currentStageIndex < Math.max(10, tempFeature.size()); currentStageIndex++){
//            if(currentStageIndex >= tempFeature.size()){
//                mutableGenerationStages.add(new ArrayList<>());
//            }else{
//                mutableGenerationStages.add(new ArrayList<>(tempFeature.get(currentStageIndex)));
//            }
//        }
//
//        // Make the Structure and GenerationStages (features) list mutable for modification later
//        ((GenerationSettingsAccessor)BuiltinBiomes.PLAINS.getGenerationSettings()).setFeatures(mutableGenerationStages);
//
//        BuiltinBiomes.PLAINS.getGenerationSettings().getFeatures().get(GenerationStep.Feature.LOCAL_MODIFICATIONS.ordinal()).add(() -> brokenTree);
    }
}
