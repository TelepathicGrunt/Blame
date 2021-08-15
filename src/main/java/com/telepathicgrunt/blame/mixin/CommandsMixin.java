package com.telepathicgrunt.blame.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.telepathicgrunt.blame.main.BrokenCommandBlame;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.IFormattableTextComponent;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/* @author - TelepathicGrunt
 *
 * Detect if a command registered is broken due to calling execute() outside a then() call.
 *
 * LGPLv3
 */
@Mixin(Commands.class)
public class CommandsMixin {

    @Shadow
    @Final
    private CommandDispatcher<CommandSource> dispatcher;

    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(method = "<init>",
            at = @At(value = "RETURN"))
    private void blame_onInit(Commands.EnvironmentType p_i232148_1_, CallbackInfo ci) {
        BrokenCommandBlame.detectBrokenCommand(dispatcher);
    }

    @Inject(method = "performCommand(Lnet/minecraft/command/CommandSource;Ljava/lang/String;)I",
            at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;isDebugEnabled()Z", remap = false),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void blame_printFailedCommandStacktrace(CommandSource commandSource, String commandString, CallbackInfoReturnable<Integer> cir,
                                                    StringReader stringreader, Exception exception,
                                                    IFormattableTextComponent iformattabletextcomponent) {
        if (!LOGGER.isDebugEnabled()) {
            BrokenCommandBlame.printStacktrace(commandString, LOGGER, exception, iformattabletextcomponent);
        }
    }
}
