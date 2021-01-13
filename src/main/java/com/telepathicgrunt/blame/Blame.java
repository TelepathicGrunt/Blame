package com.telepathicgrunt.blame;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Blame implements ModInitializer
{
    public static final String MODID = "blame";
    public static final Logger LOGGER = LogManager.getLogger();
    public static String VERSION = "2.4.0";

    @Override
    public void onInitialize() {
        FabricLoader.getInstance().getModContainer(MODID)
                .ifPresent(container -> VERSION = container.getMetadata().getVersion().toString());

        Blame.LOGGER.log(Level.ERROR, "Blame "+VERSION+" initialized");

        // Unregistered worldgen test
//        ConfiguredFeature<?,?> well = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("tgtr:fdgfg"), Feature.DESERT_WELL.configure(DefaultFeatureConfig.INSTANCE));
//
//        // Make the structure and features list mutable for modification later
//        List<List<Supplier<ConfiguredFeature<?, ?>>>> tempFeature = ((GenerationStageMixin)BuiltinBiomes.PLAINS.getGenerationSettings()).getGSFeatures();
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
//        ((GenerationStageMixin)BuiltinBiomes.PLAINS.getGenerationSettings()).setGSFeatures(mutableGenerationStages);
//
//        BuiltinBiomes.PLAINS.getGenerationSettings().getFeatures().get(GenerationStep.Feature.VEGETAL_DECORATION.ordinal()).add(() -> Feature.DESERT_WELL.configure(DefaultFeatureConfig.INSTANCE));
//        BuiltinBiomes.PLAINS.getGenerationSettings().getFeatures().get(GenerationStep.Feature.VEGETAL_DECORATION.ordinal()).add(() -> well);
//        BuiltinBiomes.PLAINS.getGenerationSettings().getFeatures().get(GenerationStep.Feature.VEGETAL_DECORATION.ordinal()).add(() -> Feature.DESERT_WELL.configure(DefaultFeatureConfig.INSTANCE));
//        BuiltinBiomes.PLAINS.getGenerationSettings().getFeatures().get(GenerationStep.Feature.VEGETAL_DECORATION.ordinal()).add(() -> well);
    }
}
