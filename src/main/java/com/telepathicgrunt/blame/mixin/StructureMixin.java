package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.StructureFeatureBlame;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* @author - TelepathicGrunt
 *
 * Detect who messed up their structure spacing/separation values
 *
 * LGPLv3
 */
@Mixin(Structure.class)
public class StructureMixin {

	@Inject(method = "getPotentialFeatureChunk(Lnet/minecraft/world/gen/settings/StructureSeparationSettings;JLnet/minecraft/util/SharedSeedRandom;II)Lnet/minecraft/util/math/ChunkPos;",
			at = @At(value = "HEAD"))
	private void checkSpacing(StructureSeparationSettings separationSettings, long seed, SharedSeedRandom rand, int x, int z, CallbackInfoReturnable<ChunkPos> cir)
	{
		if(separationSettings.spacing() == 0 || separationSettings.spacing() - separationSettings.separation() <= 0){
			StructureFeatureBlame.printStructureSpacingBlame((Structure<?>)(Object)this, separationSettings);
		}
	}
}
