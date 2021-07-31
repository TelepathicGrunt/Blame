package com.telepathicgrunt.blame.main;

import com.google.common.collect.ImmutableMap;
import com.telepathicgrunt.blame.Blame;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.unimi.dsi.fastutil.HashCommon.arraySize;

/* @author - TelepathicGrunt
 *
 * Allows us to find which mod is overriding dispenser behaviors
 *
 * LGPLv3
 */
public class DispenserBlockRegistry<K, V> extends Object2ObjectOpenHashMap<K, V> {

    // Turn on registry replacement detection only after startup's putAll I do is done.
    public Boolean startupIgnore = true;
    private static final Map<String, MessageCondenserEntry> MESSAGE_CONDENSER_MAP = Util.make(() -> {
        Map<String, MessageCondenserEntry> tempMap = new HashMap<>();

        addCondensedMessage(tempMap,
                "dispenser_configurator",
                "net.sssubtlety.dispenser_configurator.dispenserBehaviors.GenericDispenserBehavior",
                "All items specified with Dispenser Configurator's datapack will have their behavior changed. See the world's datapack folder for what item are affected by that mod.",
                "Dispenser Configurator register replaces the item's dispenser behavior to allow users to change the behavior of any item possible.");

        // Make immutable to make it less likely someone will reflect or mixin to add their own entry. They should contact me directly instead.
        return ImmutableMap.copyOf(tempMap);
    });

    @Override
    public synchronized V put(final K item, final V behavior) {

        // Check null as some stuff like ArmorItem triggers Blame before item is registered
        // Have to check for default air as that is the default value if no entry is found for the item.
        // Getting the optional RegistryKey always return null even for values that exists. Wth Mojang?
        if (!Registry.ITEM.getId((Item) item).toString().equals("minecraft:air")) {
            Identifier itemID = Registry.ITEM.getId((Item) item);

            String behaviorClassName = behavior.getClass().getName();

            if (MESSAGE_CONDENSER_MAP.containsKey(behaviorClassName)) {
                // Should be safe as we already checked above that it does contain the line
                MessageCondenserEntry entry = MESSAGE_CONDENSER_MAP.get(behaviorClassName);

                if (entry.itemBehaviorsReplaced == 0) {
                    Blame.LOGGER.log(Level.ERROR, "\n****************** Blame Report Dispenser Behavior " + Blame.VERSION + " ******************" +
                            "\n   Condensed Dispenser message mode activated for " + entry.modID + "." +
                            "\n   Dispenser behavior registry replacement was detected." +
                            "\n   Reason for the change:  " + entry.reasonForBehaviorChange +
                            "\n   The kinds of items affected:  " + entry.summaryOfItemsAffected +
                            "\n   Ignore this log entry unless item behaviors aren't working with Dispensers for some reason.\n");
                }
                entry.itemBehaviorsReplaced++;
            }
            else if (!startupIgnore && (itemID.getNamespace().equals("minecraft") || this.containsKey(item))) {
                StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
                List<StackTraceElement> stackList = new ArrayList<>();
                stackList.add(stacktrace[3]);
                stackList.add(stacktrace[4]);
                stackList.add(stacktrace[5]);

                Blame.LOGGER.log(Level.ERROR, "\n****************** Blame Report Dispenser Behavior " + Blame.VERSION + " ******************" +
                        "\n   Ignore this unless item behavior aren't working with Dispensers." +
                        "\n  Dispenser Behavior overridden for " + itemID.toString() +
                        "\n  New behavior: " + behavior.getClass().getName() +
                        "\n  Old behavior: " + this.get(item).getClass().getName() +
                        "\n  Registration done at: " +
                        "\n    " + stackList.get(0).toString() +
                        "\n    " + stackList.get(1).toString() +
                        "\n    " + stackList.get(2).toString() + "\n");
            }
        }

        final int pos = find(item);
        if (pos < 0) {
            insert(-pos - 1, item, behavior);
            return defRetValue;
        }
        final V oldValue = value[pos];
        value[pos] = behavior;
        return oldValue;
    }


    private int find(final K item) {
        if (((item) == null))
            return containsNullKey ? n : -(n + 1);
        K curr;
        final K[] key = this.key;
        int pos;
        // The starting point.
        if (((curr = key[pos = (it.unimi.dsi.fastutil.HashCommon.mix((item).hashCode())) & mask]) == null))
            return -(pos + 1);
        if (((item).equals(curr)))
            return pos;
        // There's always an unused entry.
        while (true) {
            if (((curr = key[pos = (pos + 1) & mask]) == null))
                return -(pos + 1);
            if (((item).equals(curr)))
                return pos;
        }
    }

    private void insert(final int pos, final K item, final V behavior) {
        if (pos == n)
            containsNullKey = true;
        key[pos] = item;
        value[pos] = behavior;
        if (size++ >= maxFill)
            rehash(arraySize(size + 1, f));
    }


    /**
     * For any mod to make Blame not print thousands of lines about their Dispenser Behavior registry replacement.
     * ONLY FOR MODS REPLACING 10+ BEHAVIORS. BY CONDENSING THE MESSAGES TO A SINGLE ENTRY, IT COULD END UP
     * HIDING INFO THAT MIGHT ACTUALLY HELP PEOPLE FIGURE OUT WHY AN ITEM DISPENSER BEHAVIOR IS BROKEN.
     * <p>
     * Please be VERY detailed for summaryOfItemsAffected and reasonForBehaviorChange.
     * This method can easily be abused by other mods to hide info or for condensing
     * less than 10 dispenser behaviors which is why this method will remain not exposed.
     * <p>
     * Remember, Blame is not supposed to be on 24/7. It is purely a diagnosis mod for weird worldgen crashes and bugs.
     *
     * @param modID                   The ID of the mod that wants to condense Blame's Dispenser Behavior messages about it.
     * @param stacktraceLineToDetect  The line for Blame to look for in the stacktrace to know when to condense. Example: "vazkii.quark.content.automation.module.DispensersPlaceBlocksModule"
     * @param summaryOfItemsAffected  Sentences describing what items the mod will be targeting to replace the behaviors of. If the mod has a config option to change what items are targeted, STATE THAT THE CONIG OPTION EXISTS HERE TOO.
     * @param reasonForBehaviorChange Sentences stating why the mod is replacing a ton of item's dispenser behaviors so users know what the mod is trying to do.
     */
    public static void addCondensedMessage(Map<String, MessageCondenserEntry> tempMap, String modID, String stacktraceLineToDetect, String summaryOfItemsAffected, String reasonForBehaviorChange) {
        tempMap.put(stacktraceLineToDetect, new MessageCondenserEntry(modID, summaryOfItemsAffected, reasonForBehaviorChange));
    }

    private static class MessageCondenserEntry {
        private int itemBehaviorsReplaced = 0;
        private final String modID;
        private final String summaryOfItemsAffected;
        private final String reasonForBehaviorChange;

        public MessageCondenserEntry(String modID, String summaryOfItemsAffected, String reasonForBehaviorChange) {
            this.modID = modID;
            this.summaryOfItemsAffected = summaryOfItemsAffected;
            this.reasonForBehaviorChange = reasonForBehaviorChange;
        }
    }
}
