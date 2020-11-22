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
		String structureName = Structure.NAME_STRUCTURE_BIMAP.inverse().get(structureFeature);
		ResourceLocation structureName2 = Registry.STRUCTURE_FEATURE.getKey(structureFeature);

		// Add extra info to the log file.
		String errorReport = "\n****************** Blame Report " + Blame.VERSION + " ******************" +
				"\n\n Detected a structure with a spacing of 0 which will cause a crash." +
				"\n Attempt 1 to find structure name:  " + structureName +
				"\n Attempt 2 to find structure name:  " + structureName2 +
				"\n Please let the mod author or datapack dev know about this so they can fix this.\n";
		Blame.LOGGER.log(Level.ERROR, errorReport);
	}
}
