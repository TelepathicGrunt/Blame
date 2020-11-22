package com.telepathicgrunt.blame.mixin;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.telepathicgrunt.blame.main.MissingNBTBlame;
import com.telepathicgrunt.blame.main.StructureFeatureBlame;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* @author - TelepathicGrunt
 *
 * Detect who set their structure spacing to 0
 *
 * LGPLv3
 */
@Mixin(StructureFeature.class)
public class StructureFeatureMixin {

	@Inject(method = "getStartChunk(Lnet/minecraft/world/gen/chunk/StructureConfig;JLnet/minecraft/world/gen/ChunkRandom;II)Lnet/minecraft/util/math/ChunkPos;",
			at = @At(value = "HEAD"))
	private void checkSpacing(StructureConfig config, long worldSeed, ChunkRandom placementRandom, int chunkX, int chunkY, CallbackInfoReturnable<ChunkPos> cir)
	{
		if(config.getSpacing() == 0){
			StructureFeatureBlame.printStructureSpacingBlame((StructureFeature<?>)(Object)this);
		}
	}
}
