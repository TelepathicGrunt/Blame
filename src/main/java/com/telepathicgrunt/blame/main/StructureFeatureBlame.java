package com.telepathicgrunt.blame.main;

import com.telepathicgrunt.blame.Blame;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.Structure;
import org.apache.logging.log4j.Level;

/* @author - TelepathicGrunt
 *
 * Detect who set their structure spacing to 0
 *
 * LGPLv3
 */
public class StructureFeatureBlame {

	public static void printStructureSpacingBlame(Structure<?> structureFeature)
	{
		ResourceLocation structureRL = Registry.STRUCTURE_FEATURE.getKey(structureFeature);

		// Add extra info to the log file.
		String errorReport = "\n****************** Blame Report " + Blame.VERSION + " ******************" +
				"\n\n Detected a structure with a spacing of 0 which will cause a crash." +
				"\n Attempt 1 to find structure name:  " + structureFeature.getStructureName() +
				"\n Attempt 2 to find structure name:  " + structureRL +
				"\n Check the mod's config to make sure the structure spacing entries aren't set to 0." +
				"\n If you cannot find or fix the spacing with the config, please let the mod author or " +
				"\n datapack dev know about this so they can fix this.\n";
		Blame.LOGGER.log(Level.ERROR, errorReport);
	}
}
