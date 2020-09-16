package com.telepathicgrunt.blame.mixin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/*
 * Mixin inspired by shartte's DebugWorldGenIssues. Credit goes to him!
 * https://github.com/shartte/DebugWorldGenIssues/blob/d0439df8d43e698863c153bde42eece3318da85c/src/main/java/debugworldgen/mixin/DynamicRegistryManagerMixin.java#L14-L18
 *
 * I added check to make sure the message is only printed to logs if
 * DynamicRegistries is classloaded in non-vanilla places and added
 * info to the error message for those to read.
 */
@Mixin(value = DynamicRegistries.class, priority = 99999)
public class DynamicRegistryMixin {
	@Inject(method = "<clinit>", at = @At("TAIL"), require = 1)
	private static void onInit(CallbackInfo ci) {
		//gets the details of what method classloaded DynamicRegistries
		StackTraceElement stack = Thread.currentThread().getStackTrace()[3];

		// vanilla loaded DynamicRegistries safely. No panic.
		// Did you know clients have 3 places that classloads DynamicRegistries but server has only 1?
		if((stack.getClassName().equals("net.minecraft.client.gui.screen.CreateWorldScreen") &&
			stack.getMethodName().equals("func_243425_a")) ||

			(stack.getClassName().equals("net.minecraft.client.Minecraft") &&
			stack.getMethodName().equals("func_238191_a_")) ||

			(stack.getClassName().equals("net.minecraft.server.Main") &&
			stack.getMethodName().equals("main")) ||

			(stack.getClassName().equals("net.minecraft.client.network.play.ClientPlayNetHandler") &&
			stack.getMethodName().equals("<init>")))
		{
			return;
		}

		Blame.LOGGER.log(Level.ERROR,
				"\n****************** Blame Report ******************" +
						"\n\n Oh no! Oh god! Someone classloaded DynamicRegistries class way too early!" +
						"\n Most registry entries for other mods is broken now! Please read the following stacktrace" +
						"\n and see if you can find which mod broke the game badly and please show them this log file." +
						"\n (If you can't tell which mod, let Blame creator, TelepathicGrunt, know!)" +
						"\n\n If you are the modder notified, you may be registering/accessing the dynamic" +
						"\n registries before the world is loaded/made, please use the registeries in" +
						"\n WorldGenRegistries as that is what DynamicRegistries will copy from when " +
						"\n vanilla creates/loads the world (and is why loading DynamicRegistries early breaks all mods)\n");
		Thread.dumpStack();
	}
}
