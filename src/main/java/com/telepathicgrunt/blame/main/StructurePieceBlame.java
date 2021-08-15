package com.telepathicgrunt.blame.main;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.pool.StructurePoolElement;
import org.apache.logging.log4j.Level;

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
}
