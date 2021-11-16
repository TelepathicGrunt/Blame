package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.main.PacketBlame;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerLoginNetworkHandler.class)
public class ServerLoginNetworkHandlerMixin {
    @Inject(method = "acceptPlayer()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;disconnect(Lnet/minecraft/text/Text;)V"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void blame_printPacketError(CallbackInfo ci, ServerPlayerEntity serverPlayer, Exception exception) {
        PacketBlame.printError(exception);
    }
}
