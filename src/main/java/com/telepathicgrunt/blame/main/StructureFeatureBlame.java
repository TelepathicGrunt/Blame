package com.telepathicgrunt.blame.main;

import com.telepathicgrunt.blame.Blame;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;

import java.util.Map;

/* @author - TelepathicGrunt
 *
 * Detect who messed up their structure spacing/separation values
 * Also finds structures that aren't registered to the correct field too.
 *
 * LGPLv3
 */
public class StructureFeatureBlame {

    public static void printStructureSpacingBlame(Structure<?> structureFeature, StructureSeparationSettings separationSettings) {
        ResourceLocation structureRL = Registry.STRUCTURE_FEATURE.getKey(structureFeature);

        // Add extra info to the log file.
        String errorReport = "\n****************** Blame Report Structure Spacing " + Blame.VERSION + " ******************" +
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

    public static void verifyStructuresInRegistry(){
        for(Map.Entry<RegistryKey<Structure<?>>, Structure<?>> structure : ForgeRegistries.STRUCTURE_FEATURES.getEntries()){
            if(!Structure.STRUCTURES_REGISTRY.containsValue(structure.getValue())){
                String errorReport = "\n****************** Blame Report Processor " + Blame.VERSION + " ******************" +
                        "\n\n Found a structure not registered to the \"Structure.STRUCTURES_REGISTRY\" field (Mojmap)." +
                        "\n Modders, please add your structure to that map or else chunks cannot be saved in game anymore." +
                        "\n The missing structure from the map is: " + structure.getKey();
                Blame.LOGGER.log(Level.ERROR, errorReport);
            }
        }
    }
}
