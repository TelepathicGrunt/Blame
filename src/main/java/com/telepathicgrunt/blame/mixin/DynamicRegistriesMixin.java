package com.telepathicgrunt.blame.mixin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
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
public class DynamicRegistriesMixin {
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



	/**
	 * The main hook for the parser to work from. This will check every biomes in the
	 * DynamicRegistry to see if it has exploded due to unregistered stuff added to it.
	 *
	 * It compares to the string representation of all registered objects because if we
	 * do a simple registry.get(), there seems to be a weird issue where the object in
	 * the biome is not actually the same object as in the registry. Like the biome did
	 * its own deep copy of what's in the registry as so, .get fails despite the object
	 * actually being registered already. That's why we compare the stringified JSON
	 * results instead.
	 */
	@Inject(method = "func_239770_b_()Lnet/minecraft/util/registry/DynamicRegistries$Impl;",
			at = @At(value = "RETURN"), require = 1)
	private static void printBrokenList(CallbackInfoReturnable<DynamicRegistries.Impl> cir)
	{
		// Create a store here to minimize memory impact and let it get garbaged collected later.
		DynamicRegistries.Impl imp = cir.getReturnValue();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Set<String> registered_stuff = new HashSet<>();
		Map<String, Set<ResourceLocation>> unconfigured_stuff_map = new HashMap<>();

		// ConfiguredFeatures
		imp.func_230521_a_(Registry.field_243552_au).get().stream()
				.forEach(entry -> registered_stuff.add(ConfiguredFeature.field_242763_a
						.encode(entry, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left().get().toString()));

		for(Map.Entry<RegistryKey<Biome>, Biome> mapEntry : imp.func_230521_a_(Registry.BIOME_KEY).get().getEntries()) {
			findUnregisteredConfiguredFeatures(mapEntry, unconfigured_stuff_map, registered_stuff, gson);
		}
		printUnregisteredStuff(unconfigured_stuff_map, "ConfiguredFeature");
		registered_stuff.clear();
		unconfigured_stuff_map.clear();

		// ConfiguredStructures
		imp.func_230521_a_(Registry.field_243553_av).get().stream()
				.forEach(entry -> registered_stuff.add(StructureFeature.field_236267_a_
						.encode(entry, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left().get().toString()));

		for(Map.Entry<RegistryKey<Biome>, Biome> mapEntry : imp.func_230521_a_(Registry.BIOME_KEY).get().getEntries()) {
			findUnregisteredConfiguredStructures(mapEntry, unconfigured_stuff_map, registered_stuff, gson);
		}
		printUnregisteredStuff(unconfigured_stuff_map, "ConfiguredStructure");
		registered_stuff.clear();
		unconfigured_stuff_map.clear();

		// ConfiguredCarvers
		imp.func_230521_a_(Registry.field_243551_at).get().stream()
				.forEach(entry -> registered_stuff.add(ConfiguredCarver.field_236235_a_
						.encode(entry, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left().get().toString()));

		for(Map.Entry<RegistryKey<Biome>, Biome> mapEntry : imp.func_230521_a_(Registry.BIOME_KEY).get().getEntries()) {
			findUnregisteredConfiguredCarver(mapEntry, unconfigured_stuff_map, registered_stuff, gson);
		}
		printUnregisteredStuff(unconfigured_stuff_map, "ConfiguredStructure");
		registered_stuff.clear();
		unconfigured_stuff_map.clear();
	}

	/**
	 * Prints all unregistered configured features to the log.
	 */
	private static void findUnregisteredConfiguredFeatures(Map.Entry<RegistryKey<Biome>, Biome>  mapEntry,
														   Map<String, Set<ResourceLocation>> unregistered_feature_map,
														   Set<String> registered_stuff, Gson gson)
	{

		for(List<Supplier<ConfiguredFeature<?, ?>>> generationStageList : mapEntry.getValue().func_242440_e().func_242498_c()){
			for(Supplier<ConfiguredFeature<?, ?>> configuredFeatureSupplier : generationStageList){

				Optional<JsonElement> configuredFeatureJSON = ConfiguredFeature.field_236264_b_
						.encode(configuredFeatureSupplier, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left();

				ResourceLocation biomeID = mapEntry.getKey().func_240901_a_();

				detectAndCacheUnregisteredObject(
						unregistered_feature_map,
						registered_stuff,
						gson,
						biomeID,
						configuredFeatureJSON);
			}
		}
	}

	/**
	 * Prints all unregistered configured structures to the log.
	 */
	private static void findUnregisteredConfiguredStructures(Map.Entry<RegistryKey<Biome>, Biome>  mapEntry,
															 Map<String, Set<ResourceLocation>> unregistered_structure_map,
															 Set<String> registered_stuff, Gson gson)
	{
		for(Supplier<StructureFeature<?, ?>> configuredStructureSupplier : mapEntry.getValue().func_242440_e().func_242487_a()){

			ResourceLocation biomeID = mapEntry.getKey().func_240901_a_();
			Optional<JsonElement> configuredStructureJSON = StructureFeature.field_236267_a_
					.encode(configuredStructureSupplier.get(), JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left();

			detectAndCacheUnregisteredObject(
					unregistered_structure_map,
					registered_stuff,
					gson,
					biomeID,
					configuredStructureJSON);
		}
	}

	/**
	 * Prints all unregistered configured carver to the log.
	 */
	private static void findUnregisteredConfiguredCarver(Map.Entry<RegistryKey<Biome>, Biome>  mapEntry,
														 Map<String, Set<ResourceLocation>> unregistered_carver_map,
														 Set<String> registered_stuff, Gson gson)
	{
		for(GenerationStage.Carving carvingStage : GenerationStage.Carving.values()) {
			for (Supplier<ConfiguredCarver<?>> configuredCarverSupplier : mapEntry.getValue().func_242440_e().func_242489_a(carvingStage)) {

				ResourceLocation biomeID = mapEntry.getKey().func_240901_a_();
				Optional<JsonElement> configuredCarverJSON = ConfiguredCarver.field_236235_a_
						.encode(configuredCarverSupplier.get(), JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left();

				detectAndCacheUnregisteredObject(
						unregistered_carver_map,
						registered_stuff,
						gson,
						biomeID,
						configuredCarverJSON);
			}
		}
	}

	private static void detectAndCacheUnregisteredObject(Map<String, Set<ResourceLocation>> unregistered_structure_map,
														 Set<String> registered_stuff, Gson gson, ResourceLocation biomeID,
														 Optional<JsonElement> configuredStructureJSON)
	{
		if(!registered_stuff.contains(configuredStructureJSON.get().toString()))
		{
			String cfstring = gson.toJson(configuredStructureJSON.get());

			if(!unregistered_structure_map.containsKey(cfstring))
				unregistered_structure_map.put(cfstring, new HashSet<>());

			unregistered_structure_map.get(cfstring).add(biomeID);
		}
	}

	private static void printUnregisteredStuff(Map<String, Set<ResourceLocation>> UNREGISTERED_STUFF_MAP, String type){
		for(Map.Entry<String, Set<ResourceLocation>> entry : UNREGISTERED_STUFF_MAP.entrySet()){

			// Add extra info to the log.
			String errorReport = "\n****************** Blame Report ******************" +
					"\n\n This " + type + " was found to be not registered. Look at the JSON info and try to" +
					"\n find which mod it belongs to. Then go tell that mod owner to register their " + type +
					"\n as otherwise, it will break other mods or datapacks that registered their stuff." +
					"\n\n JSON info : " + entry.getKey() +
					"\n\n Biome affected : " + entry.getValue().toString().replaceAll("(([\\w :]*,){9})", "$1\n                  ") + "\n\n";

			// Log it to the latest.log file as well.
			Blame.LOGGER.log(Level.ERROR, errorReport);
		}
	}
}
