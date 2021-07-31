package com.telepathicgrunt.blame;

import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.MoodSoundAmbience;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TwoLayerFeature;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.foliageplacer.SpruceFoliagePlacer;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.DatagenModLoader;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Blame.MODID)
public class Blame {
    public static final String MODID = "blame";
    public static final Logger LOGGER = LogManager.getLogger();
    public static String VERSION = "N/A";

    public Blame() {

        ModList.get().getModContainerById(Blame.MODID)
                .ifPresent(container -> VERSION = container.getModInfo().getVersion().toString());

        // Do not run when datagenning
        if(DatagenModLoader.isRunningDataGen()) return;

        Blame.LOGGER.log(Level.ERROR, "Blame " + VERSION + " initialized");

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
                () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.LOWEST, this::afterModStartups);

        // Test code biomes stuff to see what missing stuff does to the logs
        //FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Biome.class, this::registerBiome);

        // Test detecting broken commands that called .execute() outside a .then() call.
        //MinecraftForge.EVENT_BUS.addListener(this::registerCommands);

        // Test detecting dispenser registry replacements
        // DispenserBlock.registerDispenseBehavior(Items.HONEY_BOTTLE, new DefaultDispenseItemBehavior());

        // Test detecting unregistered configuredfeatures.
        // MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::biomeModification);

        // Test detecting broken configuredfeatures.
        // MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::brokenConfiguredFeature);
    }

    // Let us know when TagCollectionManager is safe to be classloaded.
    public static boolean MAIN_MOD_STARTUPS_FINISHED = false;

    private void afterModStartups(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            MAIN_MOD_STARTUPS_FINISHED = true;
        });
    }

//    private void registerBiome(final RegistryEvent.Register<Biome> event){
//        Biome biome = (new Biome.Builder())
//                .precipitation(Biome.RainType.RAIN)
//                .biomeCategory(Biome.Category.FOREST)
//                .depth(50)
//                .scale(50)
//                .temperature(0.6F)
//                .downfall(0.6F)
//                .specialEffects((new BiomeAmbience.Builder())
//                        .waterColor(4204)
//                        .waterFogColor(6011)
//                        .fogColor(8463)
//                        .skyColor(4446)
//                        .build())
//                .mobSpawnSettings(new MobSpawnInfo.Builder().build())
//                .generationSettings(new BiomeGenerationSettings.Builder()
//                        .surfaceBuilder(ConfiguredSurfaceBuilders.GRASS).build())
//                .build();
//        biome.setRegistryName(new ResourceLocation(Blame.MODID, "test_biome"));
//        event.getRegistry().register(biome);
//    }

//    private void registerCommands(final RegisterCommandsEvent event)
//    {
//        register(event.getDispatcher());
//    }
//
//    private static void register(CommandDispatcher<CommandSource> p_241046_0_) {
//        p_241046_0_.register(Commands.literal("locatebiome2").requires((p_241048_0_) -> {
//            return p_241048_0_.hasPermission(2);
//        }).then(Commands.argument("biome", ResourceLocationArgument.id()).suggests(SuggestionProviders.AVAILABLE_BIOMES)).executes((p_241047_0_) -> {
//            return locateBiome(p_241047_0_.getSource(), p_241047_0_.getArgument("biome", ResourceLocation.class));
//        }));
//    }
//
//    private static int locateBiome(CommandSource p_241049_0_, ResourceLocation p_241049_1_) throws CommandSyntaxException {
    // throw new RuntimeException("example exception");
//        Biome biome = p_241049_0_.getServer().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getOptional(p_241049_1_).orElseThrow(() -> {
//            return ERROR_INVALID_BIOME.create(p_241049_1_);
//        });
//        BlockPos blockpos = new BlockPos(p_241049_0_.getPosition());
//        BlockPos blockpos1 = p_241049_0_.getLevel().findNearestBiome(biome, blockpos, 6400, 8);
//        String s = p_241049_1_.toString();
//        if (blockpos1 == null) {
//            throw ERROR_BIOME_NOT_FOUND.create(s);
//        } else {
//            return LocateCommand.showLocateResult(p_241049_0_, s, blockpos, blockpos1, "commands.locatebiome.success");
//        }
//    }
//    private static final DynamicCommandExceptionType ERROR_INVALID_BIOME = new DynamicCommandExceptionType((p_241052_0_) -> {
//        return new TranslationTextComponent("commands.locatebiome.invalid", p_241052_0_);
//    });
//    private static final DynamicCommandExceptionType ERROR_BIOME_NOT_FOUND = new DynamicCommandExceptionType((p_241050_0_) -> {
//        return new TranslationTextComponent("commands.locatebiome.notFound", p_241050_0_);
//    });


//    private void biomeModification(final BiomeLoadingEvent event) {
//        // Add our structure to all biomes including other modded biomes.
//        // You can filter to certain biomes based on stuff like temperature, scale, precipitation, mod id.
//        event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> Feature.DESERT_WELL.configured(new NoFeatureConfig()));
//    }

//
//    private void brokenConfiguredFeature(final BiomeLoadingEvent event) {
//        // Add our structure to all biomes including other modded biomes.
//        // You can filter to certain biomes based on stuff like temperature, scale, precipitation, mod id.
//        event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION).add(() ->
//                Feature.TREE.configured((
//                        new BaseTreeFeatureConfig.Builder(
//                                new SimpleBlockStateProvider(Blocks.ACACIA_LOG.defaultBlockState()),
//                                new SimpleBlockStateProvider(Blocks.ACACIA_LEAVES.defaultBlockState()),
//                                new SpruceFoliagePlacer(FeatureSpread.of(2, 1),
//                                        FeatureSpread.of(0, 2),
//                                        FeatureSpread.of(1, 1)),
//                                new StraightTrunkPlacer(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE),
//                                new TwoLayerFeature(2, 0, 2)))
//                        .ignoreVines()
//                        .build()));
//    }
}
