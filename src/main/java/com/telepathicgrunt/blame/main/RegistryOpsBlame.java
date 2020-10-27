package com.telepathicgrunt.blame.main;

import com.mojang.serialization.DataResult;
import com.telepathicgrunt.blame.Blame;
import com.telepathicgrunt.blame.utils.ErrorHints;
import com.telepathicgrunt.blame.utils.PrettyPrintBrokenJSON;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;
import org.apache.logging.log4j.Level;

/* @author - TelepathicGrunt
 *
 * A mixin to make Minecraft actually tell me which
 * datapack json file broke. SPEAK TO ME MINECRAFT!
 *
 * LGPLv3
 */
public class RegistryOpsBlame {

	private static Identifier CURRENT_IDENTIFIER;

	/**
	 * Grabs the current file we are at to pass to next mixin in case file explodes.
	 */
	public static void getCurrentFile(Identifier identifier)
	{
		CURRENT_IDENTIFIER = identifier;
	}

	/**
	 * Checks if the loaded datapack file errored and print it's resource location if it did
	 */
	public static <E> void addBrokenFileDetails(DataResult<SimpleRegistry<E>> dataresult)
	{
		if(dataresult.error().isPresent()){
			String brokenJSON = null;
			String reason = null;

			// Attempt to pull the JSON out of the error message if it exists.
			// Has a try/catch in case there's an error message that somehow breaks the string split.
			if(dataresult.error().isPresent()){
				try{
					String[] parsed = dataresult.error().get().message().split(": \\{", 2);
					reason = parsed[0];
					brokenJSON = "{" + parsed[1];
				}
				catch(Exception e){
					brokenJSON = "Failed to turn error msg into string. Please notify " +
							"TelepathicGrunt (Blame creator) and show him this message:  \n" + dataresult.error().get().message();
				}
			}

			Blame.LOGGER.log(Level.ERROR,
					"\n****************** Blame Report " + Blame.VERSION + " ******************"
					+ "\n\n Failed to load resource file: "+ CURRENT_IDENTIFIER
					+ "\n\n Reason stated: " + reason
					+ "\n\n Possibly helpful hint (hopefully): " + ErrorHints.HINT_MAP.getOrDefault(reason, "If this is a worldgen JSON file, check out slicedlime's example datapack\n   for worldgen to find what's off about the JSON: https://t.co/cm3pJcAHcy?amp=1")
					+ "\n\n Prettified JSON: \n" + (brokenJSON != null ? PrettyPrintBrokenJSON.prettyPrintJSONAsString(brokenJSON) : " Unable to display JSON. ")
					+ "\n\n"
					);

		}
	}

}
