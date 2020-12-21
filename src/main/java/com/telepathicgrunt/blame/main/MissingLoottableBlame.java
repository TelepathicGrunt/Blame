package com.telepathicgrunt.blame.main;

import com.telepathicgrunt.blame.Blame;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import java.util.HashSet;
import java.util.Set;

/* @author - TelepathicGrunt
 *
 * Will print out any loottable that doesn't exist.
 *
 * LGPLv3
 */
public class MissingLoottableBlame {

	// Prevent log spam if one mod keeps attempting to get the missing loottable.
	private static final Set<String> PRINTED_RLS = new HashSet<>();

	public static void addMissingLoottableDetails(ResourceLocation miniRL)
	{
		if(miniRL == null) return; // Some mods might pass null for the loottable wtf.

		String fullPath = "data/" + miniRL.getNamespace() + "/loot_tables/" + miniRL.getPath() + ".json";
		if(PRINTED_RLS.contains(fullPath)) return;

		StackTraceElement stack = Thread.currentThread().getStackTrace()[4];
		StackTraceElement stack2 = Thread.currentThread().getStackTrace()[5];

		// Stop loottable logspam from jeresources as it triggered missing loottable when it
		// checks other mod's entity's drops which may not have any loottable for them.
		//
		// Skip logspam from missing loottables for mob drops and block break as that seems to be intentional by most mods.
		if((stack.getClassName().equals("jeresources") && stack.getMethodName().equals("compatibility.minecraft.MinecraftCompat.lambda$registerVanillaMobs$0")) ||
			(stack.getClassName().equals("net.minecraft.entity.LivingEntity") && (stack.getMethodName().equals("func_213354_a") || stack.getMethodName().equals("dropLoot"))) ||
			(stack.getClassName().equals("block.AbstractBlock") && (stack.getMethodName().equals("func_220076_a") || stack.getMethodName().equals("getDrops"))))
		{
				return;
		}

		// Add extra info to the log file.
		String errorReport = "\n****************** Blame Report " + Blame.VERSION + " ******************" +
				"\n\n Found a Loot Table path that does not exist:  " + miniRL +
				"\n The path represented by this Loot Table is:  " + fullPath +
				"\n Loot Table method called at:  " + stack.getClassName() + "." + stack.getMethodName() +
				"\n                               " + stack2.getClassName() + "." + stack2.getMethodName() +
				"\n Most common cause is that the Loot Table file is not actually at that location." +
				"\n Please let the mod author know about this so they can check to see if their Loot Table is correct.\n";
		Blame.LOGGER.log(Level.ERROR, errorReport);

		PRINTED_RLS.add(fullPath);
	}
}
