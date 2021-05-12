package com.telepathicgrunt.blame.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.telepathicgrunt.blame.main.BrokenCommandBlame;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
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
@Mixin(CommandManager.class)
public class CommandManagerMixin {

	@Shadow
	@Final
	private CommandDispatcher<CommandSource> dispatcher;

	@Shadow
	@Final
	private static Logger LOGGER;

	@Inject(method = "<init>",
			at = @At(value = "RETURN"))
	private void onInit(CommandManager.RegistrationEnvironment environment, CallbackInfo ci) {
		BrokenCommandBlame.detectBrokenCommand(dispatcher);
	}

	@Inject(method = "execute(Lnet/minecraft/server/command/ServerCommandSource;Ljava/lang/String;)I",
			at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;isDebugEnabled()Z"),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void printFailedCommandStacktrace(ServerCommandSource commandSource, String commandString, CallbackInfoReturnable<Integer> cir,
											  StringReader stringreader, Exception exception, MutableText mutableText)
	{
		if(!LOGGER.isDebugEnabled()) {
			BrokenCommandBlame.printStacktrace(commandString, LOGGER, exception, mutableText);
		}
	}
}
