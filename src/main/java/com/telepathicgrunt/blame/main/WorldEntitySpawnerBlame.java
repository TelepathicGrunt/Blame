package com.telepathicgrunt.blame.main;

import com.telepathicgrunt.blame.Blame;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import org.apache.logging.log4j.Level;

import java.util.List;

/* @author - TelepathicGrunt
 *
 * Detect what mob is crashing the game with a 0 or negative list mob.
 *
 * LGPLv3
 */
public class WorldEntitySpawnerBlame {

    public static void addMobCrashDetails(IServerWorld serverWorld, EntityClassification entityClassification, BlockPos pos, Biome biome, List<MobSpawnInfo.Spawners> list) {
        // Figure out if mob spawning is gonna crash game and to run our code if so.
        int totalWeight = WeightedRandom.getTotalWeight(list);
        if (totalWeight <= 0) {

            RegistryKey<World> worldID = serverWorld.getLevel().dimension();
            ResourceLocation biomeID = serverWorld.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome);

            // Add extra info to the log file.
            Blame.LOGGER.log(Level.ERROR, "\n\n****************** Blame Report Mob Weights " + Blame.VERSION + " ******************" +
                    "\n  Detected total weight of mob list is 0 or negative which will crash the game! " +
                    "\n  See info below to find which mob is the problem and where it is attempting to spawn at." +
                    "\n World Registry Name : " + worldID.location() +
                    "\n Biome Registry Name : " + (biomeID != null ? biomeID.toString() : "Wait what? How is the biome not registered and has no registry name!?!? This should be impossible!!!") +
                    "\n Classification of entity being spawned : " + entityClassification.getName() +
                    "\n Entity position : " + pos.toString() +
                    "\n Weighted list of mobs to spawn : " + printMobListContents(list) +
                    "\n");

        }
    }

    public static void addMobGroupCrashDetails(IServerWorld serverWorld, EntityClassification entityClassification, BlockPos pos, Biome biome, MobSpawnInfo.Spawners spawner) {
        // Figure out if mob spawning is gonna crash game and to run our code if so.
        if (spawner!= null && spawner.minCount > spawner.maxCount) {

            RegistryKey<World> worldID = serverWorld.getLevel().dimension();
            ResourceLocation biomeID = serverWorld.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome);

            // Add extra info to the log file.
            Blame.LOGGER.log(Level.ERROR, "\n\n****************** Blame Report Mob Groups " + Blame.VERSION + " ******************" +
                    "\n  Detected a mob with a minGroup size larger than maxGroup size which will crash the game! minGroup must always be smaller." +
                    "\n  See info below to find which mob is the problem and where it is attempting to spawn at." +
                    "\n World Registry Name : " + worldID.location() +
                    "\n Biome Registry Name : " + (biomeID != null ? biomeID.toString() : "Wait what? How is the biome not registered and has no registry name!?!? This should be impossible!!!") +
                    "\n Classification of entity being spawned : " + entityClassification.getName() +
                    "\n Entity position : " + pos.toString() +
                    "\n Mobs attempted to be spawn : " + spawner +
                    "\n");

        }
    }

    private static String printMobListContents(List<MobSpawnInfo.Spawners> list) {
        StringBuilder contents = new StringBuilder();

        for (MobSpawnInfo.Spawners spawners : list) {
            contents.append("\n    [").append(spawners.toString()).append("]");
        }

        return contents.toString();
    }
}
