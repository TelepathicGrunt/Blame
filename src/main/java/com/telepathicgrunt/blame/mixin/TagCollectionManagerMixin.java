package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.Blame;
import com.telepathicgrunt.blame.main.BiomeBlame;
import net.minecraft.crash.CrashReport;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

/* @author - TelepathicGrunt
 *
 * Finds who may be loading the TagCollectionManager too early and could be
 * breaking the tags for others mods that register their tags afterwards.
 *
 * LGPLv3
 */
@Mixin(TagCollectionManager.class)
public class TagCollectionManagerMixin {

	@Inject(method = "getManager",
			at = @At(value = "TAIL"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private static void possibleClassloadPoint1(CallbackInfoReturnable<ITagCollectionSupplier> cir)
	{
		if(!Blame.MAIN_MOD_STARTUPS_FINISHED){
			Blame.LOGGER.log(Level.ERROR, "\n****************** Blame Report " + Blame.VERSION + " ******************" +
					"\n   TagCollectionManager was classloaded too early! " +
					"\n This can break any tag registered by other mods in code after this point." +
					"\n Please check this stacktrace and notify whoever is classloading to early to move their code later." +
					"\n TagCollectionManager should only be called after the server is made as only then, the actual tags exists.");
			Thread.dumpStack();
		}
	}

	@Inject(method = "setManager",
			at = @At(value = "TAIL"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private static void possibleClassloadPoint2(ITagCollectionSupplier managerIn, CallbackInfo ci)
	{
		if(!Blame.MAIN_MOD_STARTUPS_FINISHED){
			Blame.LOGGER.log(Level.ERROR, "\n****************** Blame Report " + Blame.VERSION + " ******************" +
					"\n   TagCollectionManager was classloaded too early! " +
					"\n This can break any tag registered by other mods in code after this point." +
					"\n Please check this stacktrace and notify whoever is classloading to early to move their code later." +
					"\n TagCollectionManager should only be called after the server is made as only then, the actual tags exists.");
			Thread.dumpStack();
		}
	}
}
