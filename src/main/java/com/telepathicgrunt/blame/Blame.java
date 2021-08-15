package com.telepathicgrunt.blame;

import com.telepathicgrunt.blame.main.StructureFeatureBlame;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Blame implements ModInitializer, DedicatedServerModInitializer, ClientModInitializer {
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


    // These two methods run after onInitialize() so that we can verify all mod's structures after the main setup.
    @Override
    public void onInitializeServer() {
        StructureFeatureBlame.verifyStructuresInRegistry();
    }

    @Override
    public void onInitializeClient() {
        StructureFeatureBlame.verifyStructuresInRegistry();
    }
}
