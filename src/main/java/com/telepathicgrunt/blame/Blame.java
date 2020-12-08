package com.telepathicgrunt.blame;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Blame.MODID)
public class Blame
{
    public static final String MODID = "blame";
    public static final Logger LOGGER = LogManager.getLogger();
    public static String VERSION = "1.7.4";

    public Blame() {
        ModList.get().getModContainerById(Blame.MODID)
                .ifPresent(container -> VERSION = container.getModInfo().getVersion().toString());

        ForgeRegistries.STRUCTURE_FEATURES.getValue(new ResourceLocation("repurposed_structures:stronghold_stonebrick"));
        ForgeRegistries.STRUCTURE_FEATURES.getValue(new ResourceLocation("repurposed_structures:stronghold_nether"));
        Blame.LOGGER.log(Level.ERROR, "Blame "+VERSION+" initialized");

        // Test detecting unregistered configuredfeatures.
        // MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::biomeModification);
    }
    
    public void biomeModification(final BiomeLoadingEvent event) {
        // Add our structure to all biomes including other modded biomes.
        // You can filter to certain biomes based on stuff like temperature, scale, precipitation, mod id.
        event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> Feature.DESERT_WELL.withConfiguration(new NoFeatureConfig()));
    }
}
