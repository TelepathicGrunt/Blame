package com.telepathicgrunt.blame.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.telepathicgrunt.blame.main.StructurePieceBlame;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/* @author - TelepathicGrunt
 *
 * Find what structure piece died when trying to save itself.
 * Very odd that this kind of crash was accomplished but surely enough, someone pulled it off lol
 *
 * LGPLv3
 */
@Mixin(AbstractVillagePiece.class)
public class AbstractVillagePieceMixin {
    @Redirect(method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundNBT;)V",
            at = @At(value = "INVOKE",
                    target = "Lcom/mojang/serialization/Codec;encodeStart(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
                    remap = false))
    private <T, A> DataResult<T> blame_structurePieceNBT(Codec<JigsawPiece> codec, DynamicOps<T> ops, A input, CompoundNBT compoundNBT) {
        return StructurePieceBlame.findBrokenStructurePiece(codec, ops, (JigsawPiece)input, (AbstractVillagePiece)(Object)this);
    }
}
