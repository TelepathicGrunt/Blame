package com.telepathicgrunt.blame.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.telepathicgrunt.blame.main.BrokenCommandBlame;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* @author - TelepathicGrunt
 *
 * Detect if a command registered is broken due to calling execute() outside a then() call.
 *
 * LGPLv3
 */
@Mixin(CommandManager.class)
public class CommandManagerMixin<S> {

	@Shadow
	@Final
	private CommandDispatcher<CommandSource> dispatcher;

	@Inject(method = "<init>",
			at = @At(value = "RETURN"),
			remap = false)
	private void onInit(CommandManager.RegistrationEnvironment environment, CallbackInfo ci)
	{
		BrokenCommandBlame.detectBrokenCommand(dispatcher);
	}
}
