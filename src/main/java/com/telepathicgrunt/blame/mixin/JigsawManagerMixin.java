package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.MissingNBTBlame;
import javafx.util.Pair;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Random;

/* @author - TelepathicGrunt
 *
 * Make it so TemplateManager actually states what nbt file was unable to be found.
 *
 * LGPLv3
 */
@Mixin(JigsawManager.class)
public class JigsawManagerMixin {

	@Inject(method = "func_242837_a(Lnet/minecraft/util/registry/DynamicRegistries;Lnet/minecraft/world/gen/feature/structure/VillageConfig;Lnet/minecraft/world/gen/feature/jigsaw/JigsawManager$IPieceFactory;Lnet/minecraft/world/gen/ChunkGenerator;Lnet/minecraft/world/gen/feature/template/TemplateManager;Lnet/minecraft/util/math/BlockPos;Ljava/util/List;Ljava/util/Random;ZZ)V",
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/gen/feature/jigsaw/JigsawPattern;getRandomPiece(Ljava/util/Random;)Lnet/minecraft/world/gen/feature/jigsaw/JigsawPiece;"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private static void storeCurrentPool(DynamicRegistries dynamicRegistries, VillageConfig villageConfig,
											 JigsawManager.IPieceFactory iPieceFactory, ChunkGenerator chunkGenerator,
											 TemplateManager templateManager, BlockPos blockPos,
											 List<? super AbstractVillagePiece> pieces_list, Random random,
											 boolean b, boolean b1, CallbackInfo ci, MutableRegistry<JigsawPattern> mutableregistry,
											 Rotation rotation, JigsawPattern jigsawpattern)
	{
		MissingNBTBlame.CALLING_POOL = jigsawpattern.getName();
	}
}
