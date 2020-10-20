package com.telepathicgrunt.blame;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Blame.MODID)
public class Blame
{
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
