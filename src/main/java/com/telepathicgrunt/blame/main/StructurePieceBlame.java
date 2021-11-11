package com.telepathicgrunt.blame.main;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.telepathicgrunt.blame.Blame;
import com.telepathicgrunt.blame.mixin.SinglePoolElementAccessor;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;

import java.util.Optional;

/* @author - TelepathicGrunt
 *
 * Find which structure piece is broken so we do not get vague errors.
 *
 * LGPLv3
 */
public class StructurePieceBlame {

    public static <T> DataResult<T> findBrokenStructurePiece(Codec<StructurePoolElement> codec, DynamicOps<T> ops, StructurePoolElement input, PoolStructurePiece poolStructurePiece) {
        DataResult<T> dataResult = codec.encodeStart(ops, input);
        if(dataResult.error().isPresent()){
            String errorReport = "\n****************** Blame Report Processor " + Blame.VERSION + " ******************" +
                    "\n\n Failed to save an PoolStructurePiece to nbt for saving structure references to the chunk." +
                    "\n Broken Piece is: " + poolStructurePiece.toString();
            Blame.LOGGER.log(Level.ERROR, errorReport);
        }
        return dataResult;
    }

    public static void addNullProcessorDetails(SinglePoolElement poolElement) {
        Optional<Identifier> optionalIdentifier = ((SinglePoolElementAccessor)poolElement).blame_getTemplateID().left();
        String fullPath = "Unknown";
        if(optionalIdentifier.isPresent()) {
            fullPath = "data/" + optionalIdentifier.get().getNamespace() + "/worldgen/template_pool/" + optionalIdentifier.get().getPath();
        }

        // Add extra info to the log file.
        String errorReport = "\n****************** Blame Report Null Processors " + Blame.VERSION + " ******************" +
                "\n\n Null Processor List found from :  " + optionalIdentifier +
                "\n The path represented by this is:  " + fullPath +
                "\n Extra: " + poolElement +
                "\n Please let the mod author know about this so they can double check the path to make sure it is correct.\n";
        Blame.LOGGER.log(Level.ERROR, errorReport);
    }
}
