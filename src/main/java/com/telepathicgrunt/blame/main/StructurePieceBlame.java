package com.telepathicgrunt.blame.main;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;

import java.util.Map;

/* @author - TelepathicGrunt
 *
 * Find which structure piece is broken so we do not get vague errors.
 *
 * LGPLv3
 */
public class StructurePieceBlame {

    public static <T> DataResult<T> findBrokenStructurePiece(Codec<JigsawPiece> codec, DynamicOps<T> ops, JigsawPiece input, AbstractVillagePiece abstractVillagePiece) {
        DataResult<T> dataResult = codec.encodeStart(ops, input);
        if(dataResult.error().isPresent()){
            String errorReport = "\n****************** Blame Report Processor " + Blame.VERSION + " ******************" +
                    "\n\n Failed to save an AbstractVillagePiece to nbt for saving structure references to the chunk." +
                    "\n Broken Piece is: " + abstractVillagePiece.toString();
            Blame.LOGGER.log(Level.ERROR, errorReport);
        }
        return dataResult;
    }
}
