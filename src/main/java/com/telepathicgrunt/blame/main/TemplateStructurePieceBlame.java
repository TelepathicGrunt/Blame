package com.telepathicgrunt.blame.main;

import com.telepathicgrunt.blame.Blame;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import org.apache.logging.log4j.Level;

/* @author - TelepathicGrunt
 *
 * Detect if someone's dummy structure piece is about to cause a crash
 *
 * LGPLv3
 */
public class TemplateStructurePieceBlame {

    public static void printAboutToCrashBlame(TemplateStructurePiece piece) {

        // Add extra info to the log file.
        String errorReport = "\n****************** Blame Report Dummy Structure Template " + Blame.VERSION + " ******************" +
                "\n\n Detected a templateStructurePiece with no placementSettings which is about to cause a crash." +
                "\n Read farther down for the main Blame report that says which structure is crashing." +
                "\n Then go to the mod author of that structure and let them know their structure piece needs to set the placementSettings field.\n";
        Blame.LOGGER.log(Level.ERROR, errorReport);
    }

    public static void printAboutToCrashBlame2(TemplateStructurePiece piece) {

        // Add extra info to the log file.
        String errorReport = "\n****************** Blame Report Dummy Structure Template " + Blame.VERSION + " ******************" +
                "\n\n Detected a templateStructurePiece with no template which is about to cause a crash." +
                "\n Read farther down for the main Blame report that says which structure is crashing." +
                "\n Then go to the mod author of that structure and let them know their structure piece needs to set the template field.\n";
        Blame.LOGGER.log(Level.ERROR, errorReport);
    }
}
