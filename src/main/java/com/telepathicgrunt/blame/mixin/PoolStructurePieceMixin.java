package com.telepathicgrunt.blame.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.telepathicgrunt.blame.main.StructurePieceBlame;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.dynamic.RegistryReadingOps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/* @author - TelepathicGrunt
 *
 * Find what structure piece died when trying to save itself.
 * Very odd that this kind of crash was accomplished but surely enough, someone pulled it off lol
 *
 * LGPLv3
 */
@Mixin(PoolStructurePiece.class)
public class PoolStructurePieceMixin {
    @Redirect(method = "writeNbt(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/nbt/NbtCompound;)V",
            at = @At(value = "INVOKE",
                    target = "Lcom/mojang/serialization/Codec;encodeStart(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
                    remap = false))
    private <T, A> DataResult<T> blame_structurePieceNBT(Codec<StructurePoolElement> codec, DynamicOps<T> ops, A input, ServerWorld world, NbtCompound compoundNBT) {
        return StructurePieceBlame.findBrokenStructurePiece(codec, ops, (StructurePoolElement)input, (PoolStructurePiece)(Object)this);
    }
}
