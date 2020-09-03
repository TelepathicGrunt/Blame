package com.telepathicgrunt.blame;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Blame.MODID)
public class Blame
{
    // Directly reference a log4j logger.

    public static final String MODID = "blame";
    public static final Logger LOGGER = LogManager.getLogger();

    public Blame() {
    }
}
