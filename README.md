# BLAME
## by TelepathicGrunt

### Forge: https://www.curseforge.com/minecraft/mc-mods/blame
### Fabric: https://www.curseforge.com/minecraft/mc-mods/blame-fabric

Blame is a mod that aims to help print out more infos for errors, crashes, and issues that usually lacks any help in the logs. Blame solely exists as a diagnosis mod and will not prevent any crashes or fix anything.

Here's are the current stuff Blame can help with!

• Prints out the stacktrace for commands that crashed when used.
(Normally, modded commands do not print anything to logs when they crash)

• Will detect and print out all broken commands when starting up a world.
(This will find commands that called an .executes() outside a .then() call because they won't work)

• Prints extra detail for features or structures that crashes during worldgen.

• Logs all ConfiguredFeatures, ConfiguredStructures, ConfiguredCarvers that are not registered.
(Mods that don't register the stuff will cause other mod's stuff to break and not spawn during worldgen)

• Detect if DynamicRegistry is loaded way too early by another mod.
(Doing so causes all other mod's registered worldgen stuff to blow up such as "Unknown Biome ID" issues)

• Logs out exactly which worldgen JSON file is broken from any mod or datapack and shows its JSON too.

• Logs out any missing loot table when the loot is attempted to be generated! Check logs if a chest is empty and this mod might catch why.

• Prints out the name of the crashing structure that has its spacing value set to 0 or if it's separation value is equal to or greater than the spacing value.
(Either one of these condition being true will crash the game.)

• Condense broken Recipe and Loot Table parsing so you can share logs easier and see errors easier.

• Fixes MC-190122 bug so that the actual true number of recipes loaded and stated in logs is correct to help debugging recipes. https://bugs.mojang.com/browse/MC-190122

• Prints out exactly which structure crashed a custom/flat ChunkGenerator because it wasn't added to FlatGenerationSettings.STRUCTURES

• Will print to logs if a Jigsaw Structure attempts to access a non-existent template pool so you know when a structure is not fully generating.

• Will log out if a mod or datapack tries to access a non-existent nbt file for easier debugging.

• Will print out if a mod classloads TagCollectionManager too early and can risk blowing up the tags of other mods that register their tag afterwards.

• Will print out what block, its nbt, and what nbt file crashed a structure processor during structure generation.


### USING BLAME IN DEV ENVIRONMENT

#### Forge

In your build.gradle file, add this maven repository. This is where gradle will look for Blame's jar.
```
repositories {
  maven {
    url "https://nexus.resourcefulbees.com/repository/maven-public/"
  }
}
```

Now add this to the dependency block. Replace <version> with the Blame version you want to use such as `1.16.5-3.0.0-forge`. Check the CurseForge page for what the latest version is.
```
dependencies {
  ...
  implementation fg.deobf("com.telepathicgrunt:Blame:<version>")
}
```

Now here's the important part. Add these two properties to both of your run configs in the build.gradle file. These will allow Blame's mixins to work. After you add the properties lines, refresh Gradle and run `genEclipseRuns` or `genIntellijRuns` or `genVSCodeRuns` based on what IDE you are using.
```
minecraft {
    ...
    runs {
        client {
            ...
            property 'mixin.env.remapRefMap', 'true'
            property 'mixin.env.refMapRemappingFile', "${projectDir}/build/createSrgToMcp/output.srg"
        }

        server {
            ...
            property 'mixin.env.remapRefMap', 'true'
            property 'mixin.env.refMapRemappingFile', "${projectDir}/build/createSrgToMcp/output.srg"
        }
    }
}
```

Launch the game and Blame should be running! You can tell as Blame will print something like this to the IDE console and latest.log file:
`[16:34:10] [modloading-worker-11/ERROR] [co.te.bl.Blame/]: Blame 1.16.5-3.0.0-forge initialized`


#### Quilt/Fabric

In your build.gradle file, add this maven repository. This is where gradle will look for Blame's jar.
```
repositories {
  maven {
    url "https://nexus.resourcefulbees.com/repository/maven-public/"
  }
}
```

Now add this to the dependency block. Replace <version> with the Blame version you want to use such as `1.16.5-3.0.1-fabric`. Check the CurseForge page for what the latest version is.
```
dependencies {
  ...
  modImplementation "com.telepathicgrunt:Blame-Fabric:1.16.5-3.0.1-fabric"
}
```

Launch the game and Blame should be running! You can tell as Blame will print something like this to the IDE console and latest.log file:
`[18:25:48] [main/ERROR] (Blame) Blame 1.16.5-3.0.1-fabric initialized`