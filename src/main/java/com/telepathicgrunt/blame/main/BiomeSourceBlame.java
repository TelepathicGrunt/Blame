package com.telepathicgrunt.blame.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.telepathicgrunt.blame.Blame;
import com.telepathicgrunt.blame.mixin.BiomeProviderAccessor;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;

import java.util.List;

/* @author - TelepathicGrunt
 *
 * Make sure biome source's list of possible biomes is not null
 *
 * LGPLv3
 */
public class BiomeSourceBlame {

    public static void checkWorld(ServerWorld serverWorld) {
        BiomeProvider biomeSource = serverWorld.getChunkSource().getGenerator().getBiomeSource();
        List<Biome> biomeList = biomeSource.possibleBiomes();
        if(biomeList == null || biomeList.contains(null)){
            Blame.LOGGER.log(Level.ERROR, "\n\n****************** Blame Report Biome Source " + Blame.VERSION + " ******************" +
                    "\n Found a biome source with their possibleBiomes list either null or contains a null entry." +
                    "\n This list should always have all biomes that the biome source can spawn. Never a null value." +
                    "\n Here's the problematic dimension:" +
                    "\n     World: " + serverWorld +
                    "\n     World key: " + serverWorld.dimension() +
                    "\n     Biome Source: " + Registry.BIOME_SOURCE.getKey(((BiomeProviderAccessor)biomeSource).blame_callCodec()) + "\n");

        }
    }
}
