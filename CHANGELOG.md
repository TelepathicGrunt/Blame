    Made for Minecraft v.1.16.4
    Created by TelepathicGrunt

Gotta blame the broken mods!

------------------------------------------------
       | Blame Forge changelog |
      
   (V.1.7.2 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)
      
    Missing Loot Table:
•  Fixed crash when a mod or datapack somehow tries to pass null in for getting a loottable. 
   There's not enough info for me to find out which mod or datapack the loottable is from it seems.


   (V.1.7.1 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)
      
    Oops:
•  Fixed missing comma in mcmeta.
  
    
   (V.1.7.0 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)
      
    Structure Spacing:
•  Will print out which structure has a spacing of 0 which will crash the game.
  
    Other:
•  Blame will print "Blame 1.7.0 initialized" at start so it is clear if Blame is running or not.


   (V.1.6.0 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)

    Unregistered worldgen:
•  Fixed issue where some mod IDs would make the mod not 
  show up in the summary report of unregistered stuff.
          
    Missing Template Pools:
•  Blame will now print out the ID of the empty starting Template Pool that is crashing the game!

•  Will also now print out the ID of the jigsaw piece that has a 
   Jigsaw Block that is trying to target a missing Template Pool. 
   That was a mouthful to say lmao.

    Missing nbt file:
•  Changed wording a bit in report.
         
    Missing Loot Table:
•  Fixed typo in report.


   (V.1.5.0 Changes) (1.16.2/1.16.3/1.16.4 Minecraft)
          
    Missing Loot Table:
•  Will now print out if a non-existent loot table was attempted 
   to be generated! Check the logs if a chest is empty and this 
   mod might catch why it was empty!
    
    Unregistered worldgen:
•  Now only fires once per world entered to reduce log spam. 

•  Made the possible mod detector log be a bit easier to find. 

    Dispenser Behaviors:
•  Made Dispenser log stuff printed by Blame now match other Blame reports.


   (V.1.4.7 Changes) (1.16.2/1.16.3 Minecraft)
        
    Dispenser Behaviors:
• Will now print out extra info when someone registry override a
  dispenser behavior for an already registered behavior for an item.


   (V.1.4.6 Changes) (1.16.2/1.16.3 Minecraft)
        
    Major:
• Fixed crash with certain JVM versions.
    
    Missing nbt file:
• Show a more accurate filepath of where the missing nbt file was looked for at.


   (V.1.4.5 Changes) (1.16.2/1.16.3 Minecraft)
       
    Missing nbt file:
• Will now try its best to state what Template Pool attempted to
  spawn the missing nbt file to make it easier for modders to
  locate and fix the problem.
  
    Backend:
• Some cleaning and refactoring so if Blame is the one to crash, 
  the logs will now say the crash happened in Blame. So yes, it
  is now easier to blame Blame itself lmao.


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