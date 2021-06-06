package com.telepathicgrunt.blame.mixin;

import com.telepathicgrunt.blame.Blame;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.tags.TagCollectionManager;
import net.minecraftforge.fml.DatagenModLoader;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/* @author - TelepathicGrunt
 *
 * Finds who may be loading the TagCollectionManager too early and could be
 * breaking the tags for others mods that register their tags afterwards.
 *
 * LGPLv3
 */
@Mixin(TagCollectionManager.class)
public class TagCollectionManagerMixin {

    @Inject(method = "getInstance",
            at = @At(value = "TAIL"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private static void possibleClassloadPoint1(CallbackInfoReturnable<ITagCollectionSupplier> cir) {
        if(DatagenModLoader.isRunningDataGen()) return;
        if (!Blame.MAIN_MOD_STARTUPS_FINISHED) {
            Blame.LOGGER.log(Level.ERROR, "\n****************** Blame Report " + Blame.VERSION + " ******************" +
                    "\n   TagCollectionManager was classloaded too early! " +
                    "\n This can break any tag registered by other mods in code after this point." +
                    "\n Please check this stacktrace and notify whoever is classloading to early to move their code later." +
                    "\n TagCollectionManager should only be called after the server is made as only then, the actual tags exists.");
            Thread.dumpStack();
        }
    }

    @Inject(method = "bind",
            at = @At(value = "TAIL"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private static void possibleClassloadPoint2(ITagCollectionSupplier managerIn, CallbackInfo ci) {
        if(DatagenModLoader.isRunningDataGen()) return;
        if (!Blame.MAIN_MOD_STARTUPS_FINISHED) {
            Blame.LOGGER.log(Level.ERROR, "\n****************** Blame Report " + Blame.VERSION + " ******************" +
                    "\n   TagCollectionManager was classloaded too early! " +
                    "\n This can break any tag registered by other mods in code after this point." +
                    "\n Please check this stacktrace and notify whoever is classloading to early to move their code later." +
                    "\n TagCollectionManager should only be called after the server is made as only then, the actual tags exists.");
            Thread.dumpStack();
        }
    }
}
