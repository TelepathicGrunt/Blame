package com.telepathicgrunt.blame.main;

import com.telepathicgrunt.blame.Blame;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import org.apache.logging.log4j.Level;

import java.util.Map;

/* @author - TelepathicGrunt
 *
 * Detects if a crash is about to happen in FlatGenerationSettings
 * due to a mod's feature not being setup right for superflat.
 *
 * LGPLv3
 */
public class FlatGenerationSettingsBlame {

    /**
     * Will detect if someone added the structure spacing to the chunkgenerator without adding
     * the structure to the FlatGenerationSettings's STRUCTURES map. Doing so will cause a crash.
     * This mixin will say what structure it was that triggered it.
     */
    public static void addCrashDetails(Map<Structure<?>, StructureFeature<?, ?>> mainStructureMap, Map.Entry<Structure<?>, StructureSeparationSettings> structureEntry) {
        // This condition will cause a crash
        if (mainStructureMap.get(structureEntry.getKey()) == null) {

            ResourceLocation rl = Registry.STRUCTURE_FEATURE.getKey(structureEntry.getKey());
            String extraDetail = rl != null ? (" | " + rl) : "";

            // Add extra info to the log before crash.
            String errorReport = "\n****************** Blame Report Flat ChunkGenerator " + Blame.VERSION + " ******************" +
                    "\n\n A crash is most likely going to happen right after this report!" +
                    "\n It seems " + structureEntry.getKey().getFeatureName() + extraDetail + " is the cause because it is not added " +
                    "\n to the FlatGenerationSettings.STRUCTURES map. Please let the mod owner " +
                    "\n of that structure know about this crash. That way they can add their structure " +
                    "\n to that map since someone is trying to spawn it in a flat/custom dimension. \n\n";

            // Log it to the latest.log file as well.
            Blame.LOGGER.log(Level.ERROR, errorReport);
        }
    }
}
