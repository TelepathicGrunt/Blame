package com.telepathicgrunt.blame.main;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.blame.Blame;
import com.telepathicgrunt.blame.mixin.SingleJigsawPieceAccessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import org.apache.logging.log4j.Level;

/* @author - TelepathicGrunt
 *
 * Make it so any Jigsaw pool with an excessively large weight
 * will be printed to logs before it eats up all the RAM.
 *
 * LGPLv3
 */
public class JigsawPatternBlame {

    // Make it so any Jigsaw pool with an excessively large weight
    // will be printed to logs before it eats up all the RAM.
    public static void printExcessiveWeight(ResourceLocation name, Pair<JigsawPiece, Integer> element) {
        String fullPath = "data/" + name.getNamespace() + "/worldgen/template_pool/" + name.getPath();
        String entryName;
        if (element.getFirst() instanceof SingleJigsawPiece) {
            entryName = (((SingleJigsawPieceAccessor) element.getFirst()).blame_getTemplateRL().left().orElse(new ResourceLocation(""))).toString();
        }
        else {
            entryName = element.getFirst().toString();
        }

        // Add extra info to the log file.
        String errorReport = "\n****************** Blame Report Template Pool Weight " + Blame.VERSION + " ******************" +
                "\n\n   Detected a Template pool for a structure has a MASSIVE weight for their pieces." +
                "\n   Weights in the millions or billions will eat up a ton of RAM at startup due to" +
                "\n   an oversight by Mojang with setting up pool weights. It might even crash Minecraft!" +
                "\n\n   The path of the datapack/mod Template Pool may be at (if named correctly) :  " + fullPath +
                "\n   The problematic element is:  " + entryName +
                "\n   The weight of the element is:  " + element.getSecond() +
                "\n   Please let the mod author know about this so they can lower their piece weight to something smaller (under 10000).\n";
        Blame.LOGGER.log(Level.ERROR, errorReport);
    }
}
