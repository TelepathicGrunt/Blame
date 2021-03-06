package com.telepathicgrunt.blame.main;

import com.telepathicgrunt.blame.Blame;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import org.apache.logging.log4j.Level;

/* @author - TelepathicGrunt
 *
 * Detect who messed up their structure spacing/separation values
 *
 * LGPLv3
 */
public class StructureFeatureBlame {

    public static void printStructureSpacingBlame(Structure<?> structureFeature, StructureSeparationSettings separationSettings) {
        ResourceLocation structureRL = Registry.STRUCTURE_FEATURE.getKey(structureFeature);

        // Add extra info to the log file.
        String errorReport = "\n****************** Blame Report " + Blame.VERSION + " ******************" +
                "\n\n Detected a structure with invalid values for spacing/separation which will cause a crash." +
                "\n Attempt 1 to find structure name:  " + structureFeature.getFeatureName() +
                "\n Attempt 2 to find structure name:  " + structureRL +
                "\n Specifically, the crash happens when spacing - separation is 0 or less." +
                "\n Spacing value found: " + separationSettings.spacing() +
                "\n Separation value found: " + separationSettings.separation() +
                "\n Check the mod's config to make sure the structure spacing/separation entries " +
                "\n aren't set to 0 and that separation value is greater than spacing value." +
                "\n If you cannot find or fix the spacing/separation with the config, please let " +
                "\n the mod author or datapack dev know about this so they can fix this.\n";
        Blame.LOGGER.log(Level.ERROR, errorReport);
    }
}
