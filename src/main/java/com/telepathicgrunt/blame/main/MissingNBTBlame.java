package com.telepathicgrunt.blame.main;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import java.util.HashSet;
import java.util.Set;

/* @author - TelepathicGrunt
 *
 * Make it so TemplateManager actually states what nbt file was unable to be found.
 * Will also attempt to find the calling template pool trying to spawn the nbt file.
 *
 * LGPLv3
 */
public class MissingNBTBlame {

	// Attempt to mak eit easier to find problematic template pool missing nbt file
	private static Pair<ResourceLocation, ResourceLocation> CURRENT_RL = null;
	public static ResourceLocation CALLING_POOL = null;

	// Prevent log spam if one mod keeps attempting to get the missing nbt file.
	private static final Set<String> PRINTED_RLS = new HashSet<>();

	public static void storeCurrentIdentifiers(Pair<ResourceLocation, ResourceLocation> pieceRL)
	{
		CURRENT_RL = pieceRL;
	}

	public static void addMissingnbtDetails(ResourceLocation miniRL)
	{
		String fullPath = "data/" + miniRL.getNamespace() + "/structures/" + miniRL.getPath() + ".nbt";
		ResourceLocation parentID = null;

		if(CURRENT_RL != null && CURRENT_RL.getSecond().equals(miniRL))
			parentID = CURRENT_RL.getFirst();

		if(PRINTED_RLS.contains(parentID + fullPath)) return;

		// Add extra info to the log file.
		String errorReport = "\n****************** Blame Report " + Blame.VERSION + " ******************" +
				"\n\n Failed to load structure nbt file from:  " + miniRL +
				"\n The path represented by this is:  " + fullPath +
				(parentID != null ? "\n The calling Template Pool is: " + parentID : "") +
				"\n Most common cause is that the structure nbt file is not actually at that location." +
				"\n Please let the mod author know about this so they can move their structure nbt file to the correct place." +
				"\n A common mistake is putting the structure nbt file in the asset folder when it needs to go in data/structures folder.\n";
		Blame.LOGGER.log(Level.ERROR, errorReport);

		PRINTED_RLS.add(parentID + fullPath);
	}
}
