package com.telepathicgrunt.blame.mixin;

import com.google.common.collect.ImmutableMap;
import com.telepathicgrunt.blame.Blame;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DatagenModLoader;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Shadow
    private Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes;

    /**
     * Log a more useful message. Full stack trace is not useful. Concise, readable errors are useful.
     * `require = 0` as this is the definition of non-essential
     */
    @Redirect(method = "apply", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false), require = 0)
    private void blame_simplifyInvalidRecipeLogOutput(Logger logger, String message, Object p0, Object p1) {
        if(DatagenModLoader.isRunningDataGen()) return;
        logger.error(message + " {}: {} (Blame {}: suppressed long stacktrace)", p0, p1.getClass().getSimpleName(), ((Exception) p1).getMessage(), Blame.VERSION);
    }

    /**
     * This fixes a stupid vanilla bug - when it logs "Loaded X recipes", it actually logs the number of recipe types, not the number of recipes. `require = 0` as this is the definition of non-essential
     * See MC-190122 https://bugs.mojang.com/browse/MC-190122
     */
    @Redirect(method = "apply", at = @At(value = "INVOKE", target = "Ljava/util/Map;size()I"), require = 0)
    private int blame_redirect$apply$size(Map<IRecipeType<?>, ImmutableMap.Builder<ResourceLocation, IRecipe<?>>> map) {
        return this.recipes.values().stream().mapToInt(Map::size).sum();
    }
}
