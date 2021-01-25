    Made for Minecraft v.1.16.2/3/4
    Created by TelepathicGrunt

Gotta blame the broken mods!

------------------------------------------------
       | Blame Fabric changelog |
                 
   (V.2.4.1 Changes) (1.16.2/1.16.3/1.16.4/1.16.5 Minecraft)
   
    Dispenser Behaviors:
• Exposed a new method for other mods to hook into and use in order to condense Blame's Registry Replacement messages about their mods if it is excessive.
  The method is DispenserBlockRegistry.addCondensedMessage. Mod authors, READ THE JAVADOC FOR THE METHOD CAREFULLY.

    Structures/Features:
• Hardened error reporting so broken features/structures/registries are less likely to crash Blame before Blame can report. 
  Should make running Blame in dev environment less likely to mask actual errors.


   (V.2.4.0 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)
   
    JSON file on world loading:
•  Added extra info about missing block properties from broken ConfiguredFeatures files to help modders fix them better.
      
    Template Pools:
• Will now detect and print out if a mod or datapack has a pool element with an insanely high weight that is eating up all the RAM.
   
    Backend:
-Prefixed all my accessor mixins due to this bug in mixins that could cause a crash with other mods for same named mixins.
 https://github.com/SpongePowered/Mixin/issues/430
 
      
   (V.2.3.0 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)
      
    Mob Spawning:
•  Will detect if mob spawning will crash due to mob entries having a weight of 0 or negative value.

    Dispenser Behaviors:
•  Fixed potential crash due to Dispenser registration not being syncronized by Mojang which makes it harder for Blame to work properly.

    Missing Loottable:
•  Fixed jeresources check not working to help reduce some logspam.
  
    Structures:
•  Now will properly print out the structure that is crashing instead of Blame itself crashing lol.


   (V.2.2.6 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)
      
    Dispenser Behaviors:
•  Fixed logspam when a mod uses ArmorItem which registers the Dispenser behavior before the item is registered.

  
   (V.2.2.5 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)
      
    Dispenser Behaviors:
•  Fixed code that crashes any dispensed item that doesn't have a default behavior because I was
   an idiot and forgot to set default behavior for those items like vanilla does. GG TelepathicGrunt. 
   
•  Upgraded registry replacement detection to detect if a new behavior was assigned to Vanilla items without behaviors.

    Missing Loot Table:
•  Removed logspam from missing loottables for dying mobs or breaking blocks 
   as those missing loottables appears to be intentional by most mods.


   (V.2.2.4 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)
      
    Dispenser Behaviors:
•  Reduced the logspam from dispenser registry replacement detection.

    Missing Loot Table:
•  Added 1 extra stacktrace line to missing loottable reports in case that might help more
   on finding where it is called.

       
   (V.2.2.3 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)
      
    Unregistered worldgen:
•  Moved location for detecting unregistered worldgen to now find mods injecting into MinecraftServer.
   
       
   (V.2.2.2 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)
      
    Structure Spacing:
•  Now will detect if separation value is less than or equal 
   to spacing value because that will crash structures.
   
   
   (V.2.2.1 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)
      
    Missing Loot Table:
•  Fixed crash when a mod or datapack somehow tries to pass null in for getting a loottable. 
   There's not enough info for me to find out which mod or datapack the loottable is from it seems.
   
   
   (V.2.2.0 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)
      
    Structure Spacing:
•  Will print out which structure has a spacing of 0 which will crash the game.

    Other:
•  Blame will print "Blame 1.7.0 initialized" at start so it is clear if Blame is running or not.


   (V.2.1.2 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)
      
    Missing Template Pools:
•  Blame will now print out the ID of the empty starting Template Pool that is crashing the game!

•  Will also now print out the ID of the jigsaw piece that has a 
   Jigsaw Block that is trying to target a missing Template Pool. 
   That was a mouthful to say lmao.

    Missing nbt file:
•  Changed wording a bit in report.


   (V.2.1.1 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)
      
    Unregistered worldgen:
•  Added back in code for unregistered worldgen stuff but put in a new 
   experimental spot to run... Hopefully this won't hide actual issues.

    Missing Loot Table:
•  Fixed typo in report.


   (V.2.1.0 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)
      
    Missing Loot Table:
•  Will now print out if a non-existent loot table was attempted 
   to be generated! Check the logs if a chest is empty and this 
   mod might catch why it was empty!
   
    Unregistered worldgen:
• Removed code for printing out unregistered worldgen stuff due to
  crash with Cloth that I can't seem to figure out how to workaround.

    Dispenser Behaviors:
•  Made Dispenser log stuff printed by Blame now match other Blame reports.


   (V.2.0.0 Changes) (1.16.2/1.16.3 Minecraft)
      
    Major:
• Ported to Fabric from Forge!


   (V.1.4.4 Changes) (1.16.2/1.16.3 Minecraft)
      
    DynamicRegistries:
• Changed scary classloading warning to be more of just a small comment 
  when Vampirism loads up the DynamicRegistries as it appears they 
  classload it at a safe time.


   (V.1.4.3 Changes) (1.16.2/1.16.3 Minecraft)
      
    Flat/Custom ChunkGenerator crashes:
• Will detect if the ChunkGenerator will crash at world load and if so,
  add details to the log about which mod's structure caused the crash so
  they can add their structures to FlatGenerationSettings.STRUCTURES map. 
  
    Unregistered worldgen:
• Forgot to comment out my unregistered Desert Well test lmao. Oops. Sorry about that


   (V.1.4.2 Changes) (1.16.2/1.16.3 Minecraft)
    
    Unregistered worldgen:
• Detection of unregistered worldgen now works perfectly after
  I had an epiphany on why the registries acted weird!
  Also fixed a possible crash too and made it now list
  all the mods responsible for unregistered stuff at 
  the bottom as best as it can.


   (V.1.4.1 Changes) (1.16.2/1.16.3 Minecraft)
   
    Unregistered worldgen:
• Add a quick check to prevent crash with TownCraft modpack. 
  What was causing the crash should've been impossible which hints
  at some weirdness going on in that pack...


   (V.1.4.0 Changes) (1.16.2/1.16.3 Minecraft)
     
    Missing nbt file:
• Will now log any attempt to load any nbt file that doesn't exist
  and state what the identifier was to make it easier to find which 
  mod/datapack is not using the correct path to the nbt file.
  
    Unregistered worldgen:
• Fixed false positive spam when the stuff was indeed registered. 
  There's some weird biome/registry interactions going on that I missed.

   
   (V.1.3.0 Changes) (1.16.2/1.16.3 Minecraft)
   
    Unregistered worldgen:
• Will now detect any ConfiguredFeature, ConfiguredStructure, or ConfiguredCarver
  that is unregistered as those mods will break other mods that registered properly.


   (V.1.2.0 Changes) (1.16.2/1.16.3 Minecraft)
   
    Detection of DynamicRegistry classloading:
• Will detect if a mod classloaded DynamicRegistry way too
  early and is killing all other mods in the registry. 
  The message will print a stacktrace to help narrow down
  which mod is registering things improperly.


   (V.1.1.0 Changes) (1.16.2 Minecraft)
   
    JSON file on world loading:
• When starting up a world and a broken JSON file is found, 
  this mod will now print the resourcelocation of the broken 
  JSON file so it can be found and corrected faster! 


   (V.1.0.4 Changes) (1.16.2 Minecraft)
   
    Major:
• Full release of working Blame that doesn't crash itself! Wooooo!!!