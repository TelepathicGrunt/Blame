package com.telepathicgrunt.blame.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.blame.Blame;
import com.telepathicgrunt.blame.mixin.MinecraftServerAccessor;
import com.telepathicgrunt.blame.mixin.TemplateManagerAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.Level;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/* @author - TelepathicGrunt
 *
 * Find why an nbt file is exploding when loading blocks from the files or why a processor is blowing up.
 * Can be quite useful to find which Jigsaw Block's replacement block field is mispelled.
 *
 * LGPLv3
 */
public class ProcessorBlame {

	public static Template.BlockInfo findBrokenProcessor(StructureProcessor structureProcessor, IWorldReader world, BlockPos blockPos1, BlockPos blockPos2, Template.BlockInfo blockInfo1, Template.BlockInfo blockInfo2, PlacementSettings placementSettings, Template template) {
		try{
			return structureProcessor.process(world, blockPos1, blockPos2, blockInfo1, blockInfo2, placementSettings, template);
		}
		catch (Exception e){
			MinecraftServer minecraftServer = ServerLifecycleHooks.getCurrentServer();
			TemplateManager templateManager = ((MinecraftServerAccessor)minecraftServer).blame_gettemplateManager();
			Map<ResourceLocation, Template> templateMap = ((TemplateManagerAccessor)templateManager).blame_gettemplates();
			String brokenNBTFile = "";
			if(template != null){
				Optional<Map.Entry<ResourceLocation, Template>> optional = templateMap.entrySet().stream().filter((entry)-> entry.getValue().equals(template)).findFirst();
				if(optional.isPresent()){
					ResourceLocation templateRL = optional.get().getKey();
					brokenNBTFile = templateRL.toString();
				}
			}
			String blockBeingProcessed = blockInfo1.state.toString();
			String blockNBT = "";
			if(blockInfo1.nbt != null){
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				blockNBT = gson.toJson(blockInfo1.nbt);
			}

			String errorReport = "\n****************** Blame Report " + Blame.VERSION + " ******************" +
					"\n\n Processor blew up trying to process a block." +
					"\n Broken template is: " + brokenNBTFile +
					"\n Block being processed: " + blockBeingProcessed +
					"\n NBT of the block being processed: " + blockNBT;
			Blame.LOGGER.log(Level.ERROR, errorReport);
			throw e;
		}
	}
}
