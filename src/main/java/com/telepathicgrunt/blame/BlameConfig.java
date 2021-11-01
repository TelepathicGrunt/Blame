package com.telepathicgrunt.blame;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class BlameConfig {
    public static Boolean printDispenserBehaviorReplacements = false;

    public static void createAndReadConfig() {
        try {
            // Create and load up Blame's config right at mod init as forge is a meanie and wont load configs at mod init.
            // Do NOT do this for config controlled registration. Using configs to control what blocks, items, etc are
            // registered or not will break mods and prevent users from connecting to servers if registries mismatch. Do not do it.
            String appConfigPath = FMLPaths.CONFIGDIR.get().toString() + File.separator + "blame-forge.properties";
            File file = new File(appConfigPath);
            file.getParentFile().mkdirs();
            file.createNewFile();

            // load up props
            Properties modProps = new Properties();
            modProps.load(new FileInputStream(appConfigPath));
            String configComments = "";

            // set the config values
            BlameConfig.printDispenserBehaviorReplacements = readAndSet(modProps, "printDispenserBehaviorReplacements", BlameConfig.printDispenserBehaviorReplacements);
            configComments +=
                    "\n printDispenserBehaviorReplacements - Should Blame print out to the log all registry replaced Dispenser Behaviors?" +
                    "\n Set this to true if you noticed that Dispensers are not working properly. Might help narrow down the cause.\n";

            // write to prop file
            modProps.store(new FileOutputStream(appConfigPath), configComments);
        }
        catch (IOException e) {
            Blame.LOGGER.error("Blame: Error with {} config file. See stacktrace below for more info.", FMLPaths.CONFIGDIR.get().toString() + File.separator + "blame-forge.properties");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked cast")
    private static <T> T readAndSet(Properties modProps, String key, T fieldToSet) {
        if(!modProps.containsKey(key))
            modProps.setProperty(key, fieldToSet.toString());
        if(fieldToSet instanceof Boolean) {
            return (T)Boolean.valueOf(Boolean.parseBoolean(modProps.getProperty(key)));
        }
        else {
            return (T)modProps.getProperty(key);
        }
    }
}
