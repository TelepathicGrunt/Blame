package com.telepathicgrunt.blame;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Blame implements ModInitializer
{
    public static final String MODID = "blame";
    public static final Logger LOGGER = LogManager.getLogger();
    public static String VERSION = "1.4.7";

    @Override
    public void onInitialize() {
        FabricLoader.getInstance().getModContainer(MODID)
                .ifPresent(container -> VERSION = container.getMetadata().getVersion().toString());
    }
}
