package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.Blame;
import net.minecraft.item.Item;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.DatagenModLoader;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* @author - TelepathicGrunt
 *
 * Find who is calling createOptional too early and breaking Forge
 *
 * LGPLv3
 */
@Mixin(FluidTags.class)
public class FluidTagsMixin {

    @Inject(method = "createOptional(Lnet/minecraft/util/ResourceLocation;Ljava/util/Set;)Lnet/minecraftforge/common/Tags$IOptionalNamedTag;", remap = false,
            at = @At(value = "RETURN"))
    private static void blame_possibleCreateOptionalCall(CallbackInfoReturnable<Tags.IOptionalNamedTag<Item>> cir) {
        if(DatagenModLoader.isRunningDataGen()) return;
        if (Blame.MAIN_TAG_INIT && !Blame.MAIN_MOD_STARTUPS_FINISHED) {
            Blame.LOGGER.log(Level.ERROR, "\n****************** Blame Report Optional Tags " + Blame.VERSION + " ******************" +
                    "\n   createOptional was called too early! Please tell the modder to use .bind() instead for making tag references in code." +
                    "\n Using createOptional will break Forge and CraftTweaker. Or even make it impossible to enter a world!");
            Thread.dumpStack();
        }
    }
}
