package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.Blame;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* @author - TelepathicGrunt
 *
 * Find who is calling createOptional too early and breaking Forge
 *
 * LGPLv3
 */
@Mixin(value = ForgeRegistries.class, remap = false)
public class ForgeRegistriesMixin {

    @Inject(method = "init()V", remap = false,
            at = @At(value = "RETURN"))
    private static void blame_forgeInitTags(CallbackInfo ci) {
        Blame.MAIN_TAG_INIT = true;
    }
}
