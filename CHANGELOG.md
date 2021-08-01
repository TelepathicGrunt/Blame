### **(V.4.0.1 Changes) (1.17.1 Minecraft)**

#### Broken/Unregistered Worldgen:
Fixed the detection of broken ConfiguredFeatures so they actually get reported now.
  Stuff like giving tree trunk player a too large value for its codec will now be reported as that can break other ConfiguredFeatures during parsing

Fixed up the error messages when detecting unregistered or broken worldgen to be a little less spammy.

#### Crashing ConfiguredStructure/ConfiguredFeatures
Fixed crashing unregistered ConfiguredFeatures not having their JSON dumped for the report.

#### Misc:
Removed some unneeded and now useless mixins

Adjusted Blame Report titles to say their topic to make ctrl+f search a little easier for finding specific reports


### **(V.4.0.0 Changes) (1.17.0 Minecraft)**

#### Misc:
Ported to 1.17.0 MC