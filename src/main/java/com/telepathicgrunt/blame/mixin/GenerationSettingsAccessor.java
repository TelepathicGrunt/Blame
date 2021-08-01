package com.telepathicgrunt.blame.mixin;

import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.Supplier;

@Mixin(GenerationSettings.class)
public interface GenerationSettingsAccessor {
    @Accessor
    List<List<Supplier<ConfiguredFeature<?, ?>>>> getFeatures();

    @Mutable
    @Accessor
    void setFeatures(List<List<Supplier<ConfiguredFeature<?, ?>>>> features);
}
