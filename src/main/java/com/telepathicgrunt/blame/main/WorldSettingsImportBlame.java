package com.telepathicgrunt.blame.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult;
import com.telepathicgrunt.blame.Blame;
import com.telepathicgrunt.blame.utils.ErrorHints;
import com.telepathicgrunt.blame.utils.PrettyPrintBrokenJSON;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/* @author - TelepathicGrunt
 *
 * A mixin to make Minecraft actually tell me which
 * datapack json file broke. SPEAK TO ME MINECRAFT!
 *
 * LGPLv3
 */
public class WorldSettingsImportBlame {
    private static final Set<RegistryKey<?>> erroredResources = new HashSet<>();

    public static <E> void addBrokenFileDetails(RegistryKey<? extends Registry<E>> registryKey, ResourceLocation id, DataResult<Supplier<E>> result) {
        result.error().ifPresent(partial -> {
            // Avoid logging the same resources twice, as this method is invoked for already-parsed resources, in which case it calls the internal resource cache. Since that is private we maintain a shallow copy, by recording the resources we print as errored.
            RegistryKey<?> fullKey = RegistryKey.create(registryKey, id);
            if (erroredResources.contains(fullKey)) {
                return;
            }
            erroredResources.add(fullKey);

            String currentResource = id + ".json";
            String reason;
            String brokenJSON;

            try {
                String[] parsed = partial.message().split(": \\{", 2);
                reason = parsed[0];
                brokenJSON = "{" + parsed[1];
            }
            catch (Exception e) {
                try {
                    String[] parsed = partial.message().split("\\[", 2);
                    reason = parsed[0];
                    brokenJSON = "[" + parsed[1];
                }
                catch (Exception e2) {
                    reason = partial.message();
                    brokenJSON = partial.message();
                }
            }

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


    /**
     * Similar to how DynamicRegistryManagerBlame tries to print broken worldgen elements except it seems in 1.17,
     * that hook there will not actually get broken worldgen elements. This method's hook will. But I kept the original
     * code in DynamicRegistryManagerBlame just in case...
     */
    public static <E> void printBrokenWorldgenElement(RegistryKey<E> key, E entry, DataResult<JsonElement> dataResult, Optional<DataResult.PartialResult<JsonElement>> error) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Either<JsonElement, DataResult.PartialResult<JsonElement>> partialResult = dataResult.promotePartial(s->{}).get();
        JsonElement jsonElement = partialResult.left().orElse(JsonNull.INSTANCE);
        Blame.LOGGER.error(
                "\n****************** Blame Report Worldgen Import " + Blame.VERSION + " ******************" +
                        "\n\n Failed to parse worldgen. This can be tricky to solve but it could be from using a value that is over a hardcoded limit." +
                        "\n Here's some info to help narrow down where and what could be the broken worldgen element." +
                        "\n\n Error msg is: " + error.get().message() +
                        "\n Parent Affected: " + key + " | " + entry +
                        "\n\n Partial JSON Result: " + gson.toJson(jsonElement));
    }
}
