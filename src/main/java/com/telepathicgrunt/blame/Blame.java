package com.telepathicgrunt.blame;

import com.google.common.collect.ImmutableMap;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.command.arguments.SuggestionProviders;
import net.minecraft.command.impl.LocateCommand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(Blame.MODID)
public class Blame
{
    public static final String MODID = "blame";
    public static final Logger LOGGER = LogManager.getLogger();
    public static String VERSION = "1.10.0";

    public Blame() {
        ModList.get().getModContainerById(Blame.MODID)
                .ifPresent(container -> VERSION = container.getModInfo().getVersion().toString());

        Blame.LOGGER.log(Level.ERROR, "Blame "+VERSION+" initialized");

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
                () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.LOWEST, this::afterModStartups);

        // Test detecting broken commands that called .execute() outside a .then() call.
        //MinecraftForge.EVENT_BUS.addListener(this::registerCommands);

        // Test detecting dispenser registry replacements
        // DispenserBlock.registerDispenseBehavior(Items.HONEY_BOTTLE, new DefaultDispenseItemBehavior());

        // Test detecting unregistered configuredfeatures.
        // MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::biomeModification);
    }

    // Let us know when TagCollectionManager is safe to be classloaded.
    public static boolean MAIN_MOD_STARTUPS_FINISHED = false;
    public void afterModStartups(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            MAIN_MOD_STARTUPS_FINISHED = true;
        });
    }

//    public void registerCommands(final RegisterCommandsEvent event)
//    {
//        register(event.getDispatcher());
//    }
//
//    public static void register(CommandDispatcher<CommandSource> p_241046_0_) {
//        p_241046_0_.register(Commands.literal("locatebiome2").requires((p_241048_0_) -> {
//            return p_241048_0_.hasPermission(2);
//        }).then(Commands.argument("biome", ResourceLocationArgument.id()).suggests(SuggestionProviders.AVAILABLE_BIOMES)).executes((p_241047_0_) -> {
//            return locateBiome(p_241047_0_.getSource(), p_241047_0_.getArgument("biome", ResourceLocation.class));
//        }));
//    }
//
//    private static int locateBiome(CommandSource p_241049_0_, ResourceLocation p_241049_1_) throws CommandSyntaxException {
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
//    public static final DynamicCommandExceptionType ERROR_INVALID_BIOME = new DynamicCommandExceptionType((p_241052_0_) -> {
//        return new TranslationTextComponent("commands.locatebiome.invalid", p_241052_0_);
//    });
//    private static final DynamicCommandExceptionType ERROR_BIOME_NOT_FOUND = new DynamicCommandExceptionType((p_241050_0_) -> {
//        return new TranslationTextComponent("commands.locatebiome.notFound", p_241050_0_);
//    });


//    public void biomeModification(final BiomeLoadingEvent event) {
//        // Add our structure to all biomes including other modded biomes.
//        // You can filter to certain biomes based on stuff like temperature, scale, precipitation, mod id.
//        event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> Feature.DESERT_WELL.withConfiguration(new NoFeatureConfig()));
//    }
}
