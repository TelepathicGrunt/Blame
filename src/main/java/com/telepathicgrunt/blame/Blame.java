package com.telepathicgrunt.blame;

import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Blame.MODID)
public class Blame
{
    public static final String MODID = "blame";
    public static final Logger LOGGER = LogManager.getLogger();
    public static String VERSION = "1.9.2";

    public Blame() {
        ModList.get().getModContainerById(Blame.MODID)
                .ifPresent(container -> VERSION = container.getModInfo().getVersion().toString());

        Blame.LOGGER.log(Level.ERROR, "Blame "+VERSION+" initialized");

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
                () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        // Test detecting dispenser registry replacements
        // DispenserBlock.registerDispenseBehavior(Items.HONEY_BOTTLE, new DefaultDispenseItemBehavior());

        // Test detecting unregistered configuredfeatures.
        // MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::biomeModification);
    }
    
//    public void biomeModification(final BiomeLoadingEvent event) {
//        // Add our structure to all biomes including other modded biomes.
//        // You can filter to certain biomes based on stuff like temperature, scale, precipitation, mod id.
//        event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> Feature.DESERT_WELL.withConfiguration(new NoFeatureConfig()));
//    }
}
