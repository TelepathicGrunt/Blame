package com.telepathicgrunt.blame.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.apache.logging.log4j.Level;

/* @author - TelepathicGrunt
 *
 * Find why an nbt file is exploding when loading blocks from the files or why a processor is blowing up.
 *
 * LGPLv3
 */
public class ProcessorBlame {

	public static Structure.StructureBlockInfo findBrokenProcessor(StructureProcessor structureProcessor, WorldView world, BlockPos blockPos1, BlockPos blockPos2, Structure.StructureBlockInfo blockInfo1, Structure.StructureBlockInfo blockInfo2, StructurePlacementData placementSettings) {
		try{
			return structureProcessor.process(world, blockPos1, blockPos2, blockInfo1, blockInfo2, placementSettings);
		}
		catch (Exception e){
			String blockBeingProcessed = blockInfo1.state.toString();
			String blockNBT = "";
			if(blockInfo1.tag != null){
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				blockNBT = gson.toJson(blockInfo1.tag);
			}

			String errorReport = "\n****************** Blame Report " + Blame.VERSION + " ******************" +
					"\n\n Processor blew up trying to process a block." +
					"\n Block being processed: " + blockBeingProcessed +
					"\n NBT of the block being processed: " + blockNBT;
			Blame.LOGGER.log(Level.ERROR, errorReport);
			throw e;
		}
	}
}
