### **(V.3.3.0 Changes) (1.16.2/1.16.3/1.16.4/1.16.5 Minecraft)**

#### Empty Structure Start Pools:
Blame will print additional info if a start pool for a jigsaw structure is empty as that will cause a crash.
It will talk you through why it make be empty and who is the owner of the pool that became empty and crashed.

#### Unregistered Worldgen:
Will condense the report so that unregistered worldgen affecting all biomes will say "all biomes" instead of listing every biome.


### **(V.3.2.5 Changes) (1.16.2/1.16.3/1.16.4/1.16.5 Minecraft)**

#### Broken/Unregistered Worldgen:
Fixed up the error messages when detecting unregistered or broken worldgen to be a little less spammy.

#### Crashing ConfiguredStructure/ConfiguredFeatures
Made Blame report less spammy when reporting what ConfiguredStructure or ConfiguredFeature crashed.

Fixed crashing unregistered ConfiguredFeatures not having their JSON dumped for the report.


### **(V.3.2.4 Changes) (1.16.2/1.16.3/1.16.4/1.16.5 Minecraft)**

#### Broken ConfiguredFeatures:
Fixed the detection of broken ConfiguredFeatures so they actually get reported now.
  Stuff like giving tree trunk player a too large value for its codec will now be reported as that can break other ConfiguredFeatures during parsing

#### Misc:
Adjusted Blame Report titles to say their topic to make ctrl+f search a little easier for finding specific reports


### **(V.3.2.3 Changes) (1.16.2/1.16.3/1.16.4/1.16.5 Minecraft)**

#### Structure Piece:
Now will detect if a crash is caused by a mod's templateStructurePiece having a null placementSettings or template field. 
  This can be caused by mods trying to do a dummy structure piece but forgot about those fields.
  
Adjusted Blame Report titles to say their topic to make ctrl+f search a little easier for finding specific reports


### **(V.3.2.2 Changes) (1.16.2/1.16.3/1.16.4/1.16.5 Minecraft)**

#### Datagen:
Blame now disables itself when genData task is ran for a mod in a development environment. This prevents Blame reports from giving false positives.


### **(V.3.2.1 Changes) (1.16.2/1.16.3/1.16.4/1.16.5 Minecraft)**

#### Dynamic Registry:
Fixed classloaded Dynamic Registry report firing incorrectly for other mods for good now. It should be correct now. Sorry about that.


### **(V.3.2.0 Changes) (1.16.2/1.16.3/1.16.4/1.16.5 Minecraft)**

#### Worldgen Mob Spawning:
Added check to print more info if creature mobs crash during worldgen creation of chunks.
  
Fixed logging not actually working for crashes caused by mob spawning lists having zero or negative weight.


### **(V.3.1.0 Changes) (1.16.2/1.16.3/1.16.4/1.16.5 Minecraft)**

#### Dynamic Registry:
Fixed false positive report appearing saying the DynamicRegistry was classloaded too early when it wasn't outside of development environment.

Fixed a single Blame Report being printed multiple times because Minecraft makes a single error cascading through multiple files.

Added hint for "Unknown registry key" errors.

#### Loot Table/Recipes:
Condensed Recipe and Loot Table errors so that they do not make finding other errors way more difficult than it needs to be and helps make logs more easily sharable. 

#### Recipes:
Fixes MC-190122 bug so that the actual true number of recipes loaded is correct to help debugging recipes. 
  https://bugs.mojang.com/browse/MC-190122


### **(V.3.0.1 Changes) (1.16.2/1.16.3/1.16.4/1.16.5 Minecraft)**

#### Flat Chunk Generator:
Fixed crash with Forge v1.16.5-36.1.18 as they implemented a fix for the Flat Chunk Generator crash that Blame used to detect.


### **(V.3.0.0 Changes) (1.16.2/1.16.3/1.16.4/1.16.5 Minecraft)**

#### Maven:
Blame is now on a maven and can be downloaded! See the Readme on the github page for more info!

#### Dispenser Behaviors:
Condensed Savage and Ravage's Banner Dispenser Behavior registry replacements messages.

#### Commands:
Will detect and print out all broken commands when starting up a world. 
  Commands that called .executes() outside a .then() call will be found as they won't work.

Will print stacktrace of the command that broke when executed instead of letting Minecraft fail silently.

#### Tag Manager:
Will print out if a mod classloads TagCollectionManager too early and can risk blowing up the tags of 
  other mods that register their tag afterwards.

#### Processor:
Will print out what block, its nbt, and what nbt file crashed a processor during structure generation.

#### Missing NBT structure files:
Will no longer print a false positive report when saving new NBT files with Structure Blocks.



### **(V.1.9.2 Changes) (1.16.2/1.16.3/1.16.4/1.16.5 Minecraft)**
   
#### Dispenser Behaviors:
Added code to make adding code condensing easier in Blame. Mod Authors, PLEASE CONTACT ME if your mod register replaces 
 dispenser behaviors for more than 10 items. There will be no exposed method as it would be too easy for mods to abuse and
 ruin the entire point of Blame as a diagnosis tool. Instead, message condensing will be done on a case-by-case basis manually.

#### Structures/Features:
Hardened error reporting so broken features/structures/registries are less likely to crash Blame before Blame can report. 
  Should make running Blame in dev environment less likely to mask actual errors.

####  
### **(V.1.9.1 Changes) (1.16.2/1.16.3/1.16.4/1.16.5 Minecraft)**
   
#### Misc:
Make Blame not say servers modlist are incompatible when client with Blame connects to server without Blame. 
  Clients with Blame could still connect to said servers just fine tho.

#### ####  
### **(V.1.9.0 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)**
   
#### JSON file on world loading:
Added extra info about missing block properties from broken ConfiguredFeatures files to help modders fix them better.
#### 
#### Template Pools:
Will now detect and print out if a mod or datapack has a pool element with an insanely high weight that is eating up all the RAM.

#### Backend:
-Prefixed all my accessor mixins due to this bug in mixins that could cause a crash with other mods for same named mixins.
 https://github.com/SpongePowered/Mixin/issues/430
 
#### ####    
### **(V.1.8.1 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)**
####   
#### Dispenser Behaviors:
 Fixed Quark check not working again because Quark changed some code. This will now stop the logspam for good! Hopefully...


### **(V.1.8.0 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)**
####   
#### Mob Spawning:
 Will detect if mob spawning will crash due to mob entries having a weight of 0 or negative value.

#### Dispenser Behaviors:
 Fixed Quark check not working to help reduce some logspam.

 Fixed potential crash due to Dispenser registration not being syncronized by Mojang which makes it harder for Blame to work properly.

#### Missing Loottable:
 Fixed jeresources check not working to help reduce some logspam.

#### Structures:
 Now will properly print out the structure that is crashing instead of Blame itself crashing lol.


### **(V.1.7.6 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)**
####   
#### Dispenser Behaviors:
 Fixed logspam when a mod uses ArmorItem which registers the Dispenser behavior before the item is registered.

#### ####   
### **(V.1.7.5 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)**
####   
#### Dispenser Behaviors:
 Fixed code that crashes any dispensed item that doesn't have a default behavior because I was
   an idiot and forgot to set default behavior for those items like vanilla does. GG TelepathicGrunt. 
   
 Upgraded registry replacement detection to detect if a new behavior was assigned to Vanilla items without behaviors.

#### Missing Loot Table:
 Removed logspam from missing loottables for dying mobs or breaking blocks 
   as those missing loottables appears to be intentional by most mods.

#### ####   
### **(V.1.7.4 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)**
####   
#### Dispenser Behaviors:
 Reduced the logspam from dispenser registry replacement detection.
  
#### Missing Loot Table:
 Removed logspam when jeresources checks other mod's entities that doesn't have loottables.

 Added 1 extra stacktrace line to missing loottable reports in case that might help more
   on finding where it is called.
####  
#### DynamicRegistries:
Removed stacktrace from Classloading DynamicRegistry if it is done by Vampirism as they do it safely.


### **(V.1.7.3 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)**
####   
#### Structure Spacing:
 Now will detect if separation value is less than or equal 
   to spacing value because that will crash structures.
   
   
### **(V.1.7.2 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)**
####   
#### Missing Loot Table:
 Fixed crash when a mod or datapack somehow tries to pass null in for getting a loottable. 
   There's not enough info for me to find out which mod or datapack the loottable is from it seems.


### **(V.1.7.1 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)**
####   
#### Oops:
 Fixed missing comma in mcmeta.
  
#### 
### **(V.1.7.0 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)**
####   
#### Structure Spacing:
 Will print out which structure has a spacing of 0 which will crash the game.
  
#### Other:
 Blame will print "Blame 1.7.0 initialized" at start so it is clear if Blame is running or not.


### **(V.1.6.0 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)**

#### Unregistered worldgen:
 Fixed issue where some mod IDs would make the mod not 
  show up in the summary report of unregistered stuff.
#### ####   
#### Missing Template Pools:
 Blame will now print out the ID of the empty starting Template Pool that is crashing the game!

 Will also now print out the ID of the jigsaw piece that has a 
   Jigsaw Block that is trying to target a missing Template Pool. 
   That was a mouthful to say lmao.

#### Missing nbt file:
 Changed wording a bit in report.
#### ####  
#### Missing Loot Table:
 Fixed typo in report.


### **(V.1.5.0 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)**
#### ####   
#### Missing Loot Table:
 Will now print out if a non-existent loot table was attempted 
   to be generated! Check the logs if a chest is empty and this 
   mod might catch why it was empty!
#### 
#### Unregistered worldgen:
 Now only fires once per world entered to reduce log spam. 

 Made the possible mod detector log be a bit easier to find. 

#### Dispenser Behaviors:
 Made Dispenser log stuff printed by Blame now match other Blame reports.


### **(V.1.4.7 Changes) (1.16.2/1.16.3 Minecraft)**
#### #### 
#### Dispenser Behaviors:
Will now print out extra info when someone registry override a
  dispenser behavior for an already registered behavior for an item.


### **(V.1.4.6 Changes) (1.16.2/1.16.3 Minecraft)**
#### #### 
#### Major:
Fixed crash with certain JVM versions.
#### 
#### Missing nbt file:
Show a more accurate filepath of where the missing nbt file was looked for at.


### **(V.1.4.5 Changes) (1.16.2/1.16.3 Minecraft)**
####    
#### Missing nbt file:
Will now try its best to state what Template Pool attempted to
  spawn the missing nbt file to make it easier for modders to
  locate and fix the problem.
  
#### Backend:
Some cleaning and refactoring so if Blame is the one to crash, 
  the logs will now say the crash happened in Blame. So yes, it
  is now easier to blame Blame itself lmao.


### **(V.1.4.4 Changes) (1.16.2/1.16.3 Minecraft)**
####   
#### DynamicRegistries:
Changed scary classloading warning to be more of just a small comment 
  when Vampirism loads up the DynamicRegistries as it appears they 
  classload it at a safe time.


### **(V.1.4.3 Changes) (1.16.2/1.16.3 Minecraft)**
####   
#### Flat/Custom ChunkGenerator crashes:
Will detect if the ChunkGenerator will crash at world load and if so,
  add details to the log about which mod's structure caused the crash so
  they can add their structures to FlatGenerationSettings.STRUCTURES map. 
  
#### Unregistered worldgen:
Forgot to comment out my unregistered Desert Well test lmao. Oops. Sorry about that


### **(V.1.4.2 Changes) (1.16.2/1.16.3 Minecraft)**
#### 
#### Unregistered worldgen:
Detection of unregistered worldgen now works perfectly after
  I had an epiphany on why the registries acted weird!
  Also fixed a possible crash too and made it now list
  all the mods responsible for unregistered stuff at 
  the bottom as best as it can.


### **(V.1.4.1 Changes) (1.16.2/1.16.3 Minecraft)**
   
#### Unregistered worldgen:
Add a quick check to prevent crash with TownCraft modpack. 
  What was causing the crash should've been impossible which hints
  at some weirdness going on in that pack...


### **(V.1.4.0 Changes) (1.16.2/1.16.3 Minecraft)**
####  
#### Missing nbt file:
Will now log any attempt to load any nbt file that doesn't exist
  and state what the identifier was to make it easier to find which 
  mod/datapack is not using the correct path to the nbt file.
  
#### Unregistered worldgen:
Fixed false positive spam when the stuff was indeed registered. 
  There's some weird biome/registry interactions going on that I missed.

   
### **(V.1.3.0 Changes) (1.16.2/1.16.3 Minecraft)**
   
#### Unregistered worldgen:
Will now detect any ConfiguredFeature, ConfiguredStructure, or ConfiguredCarver
  that is unregistered as those mods will break other mods that registered properly.


### **(V.1.2.0 Changes) (1.16.2/1.16.3 Minecraft)**
   
#### Detection of DynamicRegistry classloading:
Will detect if a mod classloaded DynamicRegistry way too
  early and is killing all other mods in the registry. 
  The message will print a stacktrace to help narrow down
  which mod is registering things improperly.


### **(V.1.1.0 Changes) (1.16.2 Minecraft)**
   
#### JSON file on world loading:
When starting up a world and a broken JSON file is found, 
  this mod will now print the resourcelocation of the broken 
  JSON file so it can be found and corrected faster! 


### **(V.1.0.4 Changes) (1.16.2 Minecraft)**
   
#### Major:
Full release of working Blame that doesn't crash itself! Wooooo!!!