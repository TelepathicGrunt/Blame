package com.telepathicgrunt.blame.main;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.telepathicgrunt.blame.Blame;
import com.telepathicgrunt.blame.mixin.SingleJigsawPieceAccessor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
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
        DataResult<T> dataResult = null;
        boolean brokenProcessor = false;

        try {
            dataResult = codec.encodeStart(ops, input);
        } catch(Exception e){
            brokenProcessor = e.getStackTrace()[0].toString().contains("IStructureProcessorType");
        }

        if(brokenProcessor){
            SingleJigsawPiece singleJigsawPiece = null;
            if(abstractVillagePiece.getElement() instanceof SingleJigsawPiece){
                singleJigsawPiece = (SingleJigsawPiece) abstractVillagePiece.getElement();
            }

            String errorReport = "\n\n****************** Blame Report Processor Saving " + Blame.VERSION + " ******************" +
                    "\n Failed to save an AbstractVillagePiece to nbt for saving structure references to the chunk." +
                    "\n The cause is a processor returned null for its getType method. Please let the modder know to never return null." +
                    "\n A custom processor's getType method must always return a registered codec that creates that processor." +
                    "\n Broken Piece is: " + abstractVillagePiece +
                    "\n Processors found are: " + (singleJigsawPiece != null ? ((SingleJigsawPieceAccessor)singleJigsawPiece).blame_getProcessors().get().toString() : "") + "\n";
            Blame.LOGGER.log(Level.ERROR, errorReport);
        }
        else if(dataResult == null || dataResult.error().isPresent()){
            String errorReport = "\n\n****************** Blame Report AbstractVillagePiece " + Blame.VERSION + " ******************" +
                    "\n Failed to save an AbstractVillagePiece to nbt for saving structure references to the chunk." +
                    "\n Broken Piece is: " + abstractVillagePiece.toString() + "\n";
            Blame.LOGGER.log(Level.ERROR, errorReport);
        }

        return dataResult;
    }
}
