package com.telepathicgrunt.blame.mixin;

import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
 * --------------
 * Mixin disabled due to crash with Cloth.
 * Use DebugWorldgenIssues instead to catch unregistered worldgen stuff.
 * https://github.com/shartte/DebugWorldGenIssues/releases
 * --------------
 *
 * Mixin inspired by shartte's DebugWorldGenIssues. Credit goes to him!
 * https://github.com/shartte/DebugWorldGenIssues/blob/d0439df8d43e698863c153bde42eece3318da85c/src/main/java/debugworldgen/mixin/DynamicRegistryManagerMixin.java#L14-L18
 *
 * I added check to make sure the message is only printed to logs if
 * DynamicRegistries is classloaded in non-vanilla places and added
 * info to the error message for those to read.
 */
@Mixin(value = DynamicRegistryManager.class, priority = 99999)
public class DynamicRegistryManagerMixin {

	/**
	 * The main hook for the parser to work from. This will check every biomes in the
	 * DynamicRegistry to see if it has exploded due to unregistered stuff added to it.
	 */
	@Inject(method = "load(Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;Lnet/minecraft/util/dynamic/RegistryOps;)V",
			at = @At(value = "RETURN"), require = 1)
	private static void worldCreateHook(DynamicRegistryManager.Impl impl, RegistryOps<?> registryOps, CallbackInfo ci)
	{
		//DynamicRegistryManagerBlame.printUnregisteredWorldgenConfiguredStuff(impl);
	}
}
