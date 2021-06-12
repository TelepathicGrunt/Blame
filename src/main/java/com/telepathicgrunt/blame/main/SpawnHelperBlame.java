package com.telepathicgrunt.blame.main;

import com.telepathicgrunt.blame.Blame;
import com.telepathicgrunt.blame.mixin.PoolAccessor;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import org.apache.logging.log4j.Level;

/* @author - TelepathicGrunt
 *
 * Detect what mob is crashing the game with a 0 or negative list mob.
 *
 * LGPLv3
 */
public class SpawnHelperBlame {

    public static void addMobCrashDetails(ServerWorld serverWorld, SpawnGroup entityClassification, BlockPos pos, Biome biome, Pool<SpawnSettings.SpawnEntry> pool) {
        // Figure out if mob spawning is gonna crash game and to run our code if so.
        int totalWeight = ((PoolAccessor)pool).blame_getTotalWeight();
        if (totalWeight <= 0) {

            RegistryKey<World> worldID = serverWorld.getRegistryKey();
            Identifier biomeID = serverWorld.getRegistryManager().get(Registry.BIOME_KEY).getId(biome);

            // Add extra info to the log file.
            Blame.LOGGER.log(Level.ERROR, "\n****************** Blame Report " + Blame.VERSION + " ******************" +
                    "\n  Detected total weight of mob list is 0 or negative which will crash the game! " +
                    "\n  See info below to find which mob is the problem and where it is attempting to spawn at." +
                    "\n World Registry Name : " + worldID.getValue().toString() +
                    "\n Biome Registry Name : " + (biomeID != null ? biomeID.toString() : "Wait what? How is the biome not registered and has no registry name!?!? This should be impossible!!!") +
                    "\n Classification of entity being spawned : " + entityClassification.getName() +
                    "\n Entity position : " + pos.toString() +
                    "\n Weighted list of mobs to spawn : " + printMobListContents(pool) +
                    "\n");

        }
    }

    public static void addMobCrashDetails(ServerWorldAccess serverWorld, ChunkPos chunkPos, Biome biome, Pool<SpawnSettings.SpawnEntry> pool) {
        // Figure out if mob spawning is gonna crash game and to run our code if so.
        int totalWeight = ((PoolAccessor)pool).blame_getTotalWeight();
        if (totalWeight <= 0) {

            RegistryKey<World> worldID = serverWorld.toServerWorld().getRegistryKey();
            Identifier biomeID = serverWorld.getRegistryManager().get(Registry.BIOME_KEY).getId(biome);

            // Add extra info to the log file.
            Blame.LOGGER.log(Level.ERROR, "\n****************** Blame Report " + Blame.VERSION + " ******************" +
                    "\n  Detected total weight of mob list is 0 or negative which will crash the game! " +
                    "\n  See info below to find which mob is the problem and where it is attempting to spawn at." +
                    "\n World Registry Name : " + worldID.getValue().toString() +
                    "\n Biome Registry Name : " + (biomeID != null ? biomeID.toString() : "Wait what? How is the biome not registered and has no registry name!?!? This should be impossible!!!") +
                    "\n Classification of entity being spawned : " + SpawnGroup.CREATURE.getName() +
                    "\n Entity position :  chunk " + chunkPos.x + " " + chunkPos.z +
                    "\n Weighted list of mobs to spawn : " + printMobListContents(pool) +
                    "\n");

        }
    }

    private static String printMobListContents(Pool<SpawnSettings.SpawnEntry> pool) {
        StringBuilder contents = new StringBuilder();

        for (SpawnSettings.SpawnEntry spawners : pool.getEntries()) {
            contents.append("\n    [").append(spawners.toString()).append("]");
        }

        return contents.toString();
    }
}
