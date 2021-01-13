package com.telepathicgrunt.blame.main;

import com.telepathicgrunt.blame.Blame;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;

import java.util.HashSet;
import java.util.Set;

/* @author - TelepathicGrunt
 *
 * Help modders know if they are accessing a template pool that doesn't exist.
 *
 * LGPLv3
 */
public class MissingTemplatePoolBlame {

	// Prevent log spam if one mod keeps attempting to get the missing pool somehow.
	private static final Set<String> PRINTED_RLS = new HashSet<>();

	public static void addEmptyPoolDetails(Identifier poolStartId, Identifier callingParentPiece)
	{
		String fullPath = "data/" + poolStartId.getNamespace() + "/worldgen/template_pool/" + poolStartId.getPath();

		if(PRINTED_RLS.contains(poolStartId.toString())) return;

		// Add extra info to the log file.
		String errorReport = "\n****************** Blame Report " + Blame.VERSION + " ******************" +
				"\n\n Empty template pool found from : " + poolStartId.toString() +
				"\n The path represented by this is:  " + fullPath +
				(callingParentPiece != null ? "\n The structure piece with a Jigsaw Block that is targeting an empty pool is: " + callingParentPiece.toString() : ".json") +
				"\n Most common cause is that there is a typo in this path to the template pool which then points to a non-existent pool." +
				"\n Please let the mod author know about this so they can double check the path to make sure it is correct.\n";
		Blame.LOGGER.log(Level.ERROR, errorReport);

		PRINTED_RLS.add(poolStartId.toString());
	}
}
