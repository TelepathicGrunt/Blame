package com.telepathicgrunt.blame.main;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/* @author - TelepathicGrunt
 *
 * Make it so StructureManager actually states what nbt file was unable to be found.
 *
 * LGPLv3
 */
public class MissingNBTBlame {

    // Attempt to make it easier to find problematic template pool missing nbt file
    private static Pair<Identifier, Identifier> CURRENT_RL = null;
    public static Identifier CALLING_POOL = null;

    // Prevent log spam if one mod keeps attempting to get the missing nbt file.
    private static final Set<String> PRINTED_RLS = new HashSet<>();

    public static void storeCurrentIdentifiers(Pair<Identifier, Identifier> pieceRL) {
        CURRENT_RL = pieceRL;
    }

    public static void addMissingNbtDetails(Identifier miniRL) {
        // Skip structure block saving as it is a false positive.
        // We grab class name at runtime to make sure it works in both prod and dev.
        String structureBlockname = StructureBlockBlockEntity.class.getName();
        if (Arrays.stream(Thread.currentThread().getStackTrace()).anyMatch(element -> element.getClassName().equals(structureBlockname))) {
            return;
        }

        String fullPath = "data/" + miniRL.getNamespace() + "/structures/" + miniRL.getPath() + ".nbt";
        Identifier parentID = null;

        if (CURRENT_RL != null && CURRENT_RL.getSecond().equals(miniRL))
            parentID = CURRENT_RL.getFirst();

        if (PRINTED_RLS.contains(parentID + fullPath)) return;

        // Add extra info to the log file.
        String errorReport = "\n****************** Blame Report Structure NBT " + Blame.VERSION + " ******************" +
                "\n\n Failed to load structure nbt file from : " + miniRL +
                "\n The path represented by this is:  " + fullPath +
                (parentID != null ? "\n The calling Template Pool is: " + parentID : "") +
                "\n Most common cause is that there is a typo in this path to the nbt file." +
                "\n Please let the mod author know about this so they can double check to make sure the path is correct." +
                "\n A common mistake is putting the structure nbt file in the asset folder when it needs to go in data/structures folder." +
                (parentID == null ? "\n  Stacktrace of where this NBT file is being called by: " + Arrays.toString(Thread.currentThread().getStackTrace()) : "") +
                "\n";
        Blame.LOGGER.log(Level.ERROR, errorReport);

        PRINTED_RLS.add(parentID + fullPath);
    }
}
