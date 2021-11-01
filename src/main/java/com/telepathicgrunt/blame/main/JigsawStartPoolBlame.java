package com.telepathicgrunt.blame.main;

import com.telepathicgrunt.blame.Blame;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import org.apache.logging.log4j.Level;

/* @author - TelepathicGrunt
 *
 * Prints info about empty start pool file crashing
 *
 * LGPLv3
 */
public class JigsawStartPoolBlame {

    public static void printStartPoolCrash(JigsawPattern jigsawpattern) {
        if(jigsawpattern.size() == 0){
            // Add extra info to the log file.
            String errorReport = "\n****************** Blame Report Empty Start Pool " + Blame.VERSION + " ******************" +
                    "\n\n   Detected crash about to happen due to an empty start pool file for a Jigsaw Structure!" +
                    "\n   The pool that was empty is:   " + jigsawpattern.getName() +
                    "\n   (Contact the mod author that owns the above pool path and show them this latest.log file)"  +
                    "\n\n   The start pool for a jigsaw structure cannot be empty. Often causes of this are:" +
                    "\n      1. The pool path is incorrect." +
                    "\n      2. The pool file failed to be parsed." +
                    "\n      3. A processor in the pool file was failed to be parsed and the error nuked the pool file (most common)." +
                    "\n\n   A common trigger to cause #3 is a mod added a new property to vanilla blocks unsafely which caused the processor file to explode." +
                    "\n   Or the actual JSON for the processor file missed specifying a property for a block (typically in the \"output_state\" field)." +
                    "\n   If it is a processor that broke, search for \"Blame Report Broken Worldgen\" in this log file and see if which processor died.\n";
            Blame.LOGGER.log(Level.ERROR, errorReport);
        }
    }
}
