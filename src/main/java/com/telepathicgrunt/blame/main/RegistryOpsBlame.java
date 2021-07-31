package com.telepathicgrunt.blame.main;

import com.mojang.serialization.DataResult;
import com.telepathicgrunt.blame.Blame;
import com.telepathicgrunt.blame.utils.ErrorHints;
import com.telepathicgrunt.blame.utils.PrettyPrintBrokenJSON;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.apache.logging.log4j.Level;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/* @author - TelepathicGrunt
 *
 * A mixin to make Minecraft actually tell me which
 * datapack json file broke. SPEAK TO ME MINECRAFT!
 *
 * LGPLv3
 */
public class RegistryOpsBlame {
    private static final Set<RegistryKey<?>> erroredResources = new HashSet<>();

    /**
     * Checks if the loaded datapack file errored and print it's resource location if it did
     */
    public static <E> void addBrokenFileDetails(RegistryKey<? extends Registry<E>> registryKey, Identifier id, DataResult<Supplier<E>> result) {
        result.error().ifPresent(error -> {
            // Avoid logging the same resources twice, as this method is invoked for already-parsed resources, in which case it calls the internal resource cache. Since that is private we maintain a shallow copy, by recording the resources we print as errored.
            RegistryKey<?> fullKey = RegistryKey.of(registryKey, id);
            if (erroredResources.contains(fullKey)) {
                return;
            }
            erroredResources.add(fullKey);

            String currentResource = id + ".json";
            String brokenJSON;
            String reason;

            // Attempt to pull the JSON out of the error message if it exists.
            // Has a try/catch in case there's an error message that somehow breaks the string split.
            try {
                String[] parsed = error.message().split(": \\{", 2);
                reason = parsed[0];
                brokenJSON = "{" + parsed[1];
            }
            catch (Exception e) {
                try {
                    String[] parsed = error.message().split("\\[", 2);
                    reason = parsed[0];
                    brokenJSON = "[" + parsed[1];
                }
                catch (Exception e2) {
                    reason = error.message();
                    brokenJSON = error.message();
                }
            }

            // gets the hint that might help with the error
            String hint = null;
            if (reason != null) {
                for (Map.Entry<String, String> hints : ErrorHints.HINT_MAP.entrySet()) {
                    if (reason.contains(hints.getKey())) {
                        hint = hints.getValue();
                        break;
                    }
                }
            }
            // default hint that covers most basis.
            if (hint == null) {
                hint = "If this is a worldgen JSON file, check out slicedlime's example datapack\n   for worldgen to find what's off about the JSON: https://t.co/cm3pJcAHcy?amp=1";
            }

            Blame.LOGGER.log(Level.ERROR,
                    "\n****************** Blame Report Worldgen Import " + Blame.VERSION + " ******************"
                            + "\n\n Failed to load resource file: " + currentResource
                            + "\n\n Reason stated: " + reason
                            + "\n\n Possibly helpful hint (hopefully): " + hint
                            + "\n\n Prettified form of the broken JSON: \n" + PrettyPrintBrokenJSON.prettyPrintJSONAsString(brokenJSON)
                            + "\n\n"
            );
        });
    }

}
