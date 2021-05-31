package com.telepathicgrunt.blame.mixin;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin extends JsonReloadListener {
    @Shadow
    private Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes;

    private RecipeManagerMixin(Gson gson, String key) {
        super(gson, key);
    }

    /**
     * Log a more useful message. Full stack trace is not useful. Concise, readable errors are useful.
     * `require = 0` as this is the definition of non-essential
     */
    @Redirect(method = "apply", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false), require = 0)
    private void simplifyInvalidRecipeLogOutput(Logger logger, String message, Object p0, Object p1) {
        logger.error(message + " {}: {} (Blame: suppressed long stacktrace)", p0, p1.getClass().getSimpleName(), ((Exception) p1).getMessage());
    }

    /**
     * This fixes a stupid vanilla bug - when it logs "Loaded X recipes", it actually logs the number of recipe types, not the number of recipes. `require = 0` as this is the definition of non-essential
     * See MC-190122
     */
    @Redirect(method = "apply", at = @At(value = "INVOKE", target = "Ljava/util/Map;size()I"), require = 0)
    private int redirect$apply$size(Map<IRecipeType<?>, ImmutableMap.Builder<ResourceLocation, IRecipe<?>>> map) {
        return this.recipes.values().stream().mapToInt(Map::size).sum();
    }
}
