package com.telepathicgrunt.blame.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.telepathicgrunt.blame.main.BrokenCommandBlame;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* @author - TelepathicGrunt
 *
 * Detect if a command registered is broken due to calling execute() outside a then() call.
 *
 * LGPLv3
 */
@Mixin(Commands.class)
public class CommandsMixin<S> {

	@Shadow
	@Final
	private CommandDispatcher<CommandSource> dispatcher;

	@Inject(method = "<init>",
			at = @At(value = "RETURN"),
			remap = false)
	private void onInit(Commands.EnvironmentType p_i232148_1_, CallbackInfo ci)
	{
		BrokenCommandBlame.detectBrokenCommand(dispatcher);
	}
}
