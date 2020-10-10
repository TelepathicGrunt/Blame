package com.telepathicgrunt.blame;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Blame.MODID)
public class Blame
{
    // Directly reference a log4j logger.

    public static final String MODID = "blame";
    public static final Logger LOGGER = LogManager.getLogger();

    public Blame() {
    }

//    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
//    public static class ForgeEvents {
//        @SubscribeEvent
//        public static void triggerUnregisteredStuffMixin(final BiomeLoadingEvent event) {
//           event.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).add(() -> Feature.DESERT_WELL.withConfiguration(NoFeatureConfig.field_236559_b_));
//        }
//    }
//    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
//    public static class ForgeEvents {
//        @SubscribeEvent
//        public static void triggerMissingnbtFileMixin(final WorldEvent.Load event) {
//            if(event.getWorld() instanceof ServerWorld)
//                ((ServerWorld) event.getWorld()).getStructureTemplateManager().getTemplate(new ResourceLocation("fake_mod", "fake_nbt"));
//        }
//    }
}
