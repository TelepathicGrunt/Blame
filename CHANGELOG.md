### **(V.4.3.0 Changes) (1.17.1 Minecraft)**

#### Null Processors:
Now prints out the pool that crashed due to having a null processor list.

#### Invalid Player Data:
Will now print out what the error message is that normally would've been hidden to the logs when faced with Invalid Player Data disconnection.


### **(V.4.2.0 Changes) (1.17.1 Minecraft)**

#### Structure Registration:
Fixed report name being wrong lol. And formatted the report for Structure.STRUCTURES_REGISTRY field to be a bit better.

#### Worldgen Mob Spawning:
Will detect and print out info if the game is about to crash because a mob's minGroup size is greater than its maxGroup size during worldgen.


### **(V.4.1.0 Changes) (1.17.1 Minecraft)**

#### Structure Registration:
Will now print a Blame report if a registered Structure is not also added to the StructureFeature.STRUCTURES field (Yarn)
  Any structure not in that field will cause chunks to be unable to be saved. Which is bad. And the error for that is vague.

#### Structure Piece NBT:
Now will try to print out what structure piece failed to be converted to NBT for saving structure references to the chunk.

#### Unregistered Worldgen:
Will condense the report so that unregistered worldgen affecting all biomes will say "all biomes" instead of listing every biome.


### **(V.4.0.1 Changes) (1.17.1 Minecraft)**

#### Broken/Unregistered Worldgen:
Fixed the detection of broken ConfiguredFeatures so they actually get reported now.
  Stuff like giving tree trunk player a too large value for its codec will now be reported as that can break other ConfiguredFeatures during parsing

Removed detection of Unregistered Worldgen as I cannot get it to work on 1.17 and Fabric API should be forcing people to register anyway

#### Crashing ConfiguredStructure/ConfiguredFeatures
Fixed crashing unregistered ConfiguredFeatures not having their JSON dumped for the report.

#### Misc:
Removed some unneeded and now useless mixins

Adjusted Blame Report titles to say their topic to make ctrl+f search a little easier for finding specific reports


### **(V.4.0.0 Changes) (1.17.0 Minecraft)**

#### Misc:
Ported to 1.17.0 MC