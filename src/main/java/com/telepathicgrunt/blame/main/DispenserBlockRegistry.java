package com.telepathicgrunt.blame.main;

import com.telepathicgrunt.blame.Blame;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
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
public class DispenserBlockRegistry<K, V> extends Object2ObjectOpenHashMap<K, V>{

	/**
	 * For any mod to make Blame not print thousands of lines about their Dispenser Behavior registry replacement.
	 * ONLY USE THIS IF YOUR MOD IS REPLACING 10+ BEHAVIORS. BY CONDENSING THE MESSAGES TO A SINGLE ENTRY, YOU
	 * COULD BE HIDING INFO THAT MIGHT ACTUALLY HELP PEOPLE FIGURE OUT WHY AN ITEM DISPENSER BEHAVIOR IS BROKEN.
	 *
	 * Please be VERY detailed for summaryOfItemsAffected and reasonForBehaviorChange.
	 * If this method is abused by other mods to hide info or for condensing less than 10 dispenser behaviors,
	 * I will remove this exposed method in the future.
	 *
	 * Remember, Blame is not supposed to be on 24/7. It is purely a diagnosis mod for weird worldgen crashes and bugs.
	 *
	 * @param modID The ID of your mod that wants to condense Blame's Dispenser Behavior messages about it.
	 * @param stacktraceLineToDetect The line for Blame to look for in the stacktrace to know when to condense. Example: "vazkii.quark.content.automation.module.DispensersPlaceBlocksModule"
	 * @param summaryOfItemsAffected Sentences describing what items your mod will be targeting to replace the behaviors of. If your mod has a config option to change what items are targeted, STATE THAT THE CONIG OPTION EXISTS HERE TOO.
	 * @param reasonForBehaviorChange Sentences stating why your mod is replacing a ton of block's dispenser behaviors so users know what your mod is trying to do.
	 */
	public static void addCondensedMessage(String modID, String stacktraceLineToDetect, String summaryOfItemsAffected, String reasonForBehaviorChange){
		messageCondenserMap.put(stacktraceLineToDetect, new MessageCondenserEntry(modID, summaryOfItemsAffected, reasonForBehaviorChange));
	}


	// Turn on registry replacement detection only after startup's putAll I do is done.
	public Boolean startupIgnore = true;
	private static final Map<String, MessageCondenserEntry> messageCondenserMap = new HashMap<>();
	//static{ addCondensedMessage("quark", "vazkii.quark.content.automation.module.DispensersPlaceBlocksModule", "Detected Quark registry replacing the Dispenser behavior of all blocks.", "This is part of their DispensersPlaceBlocksModule which has config options."); }


	@Override
	public synchronized V put(final K item, final V behavior) {

		// Check null as some stuff like ArmorItem triggers Blame before item is registered
		// Have to check for default air as that is the default value if no entry is found for the item.
		// Getting the optional RegistryKey always return null even for values that exists. Wth Mojang?
		if(!Registry.ITEM.getId((Item)item).toString().equals("minecraft:air")) {
			Identifier itemID = Registry.ITEM.getId((Item) item);
			String behaviorClassName = behavior.getClass().getName();

			if(messageCondenserMap.containsKey(behaviorClassName))
			{
				MessageCondenserEntry entry = messageCondenserMap.get(behaviorClassName);
				if(entry.itemBehaviorsReplaced == 0){
					Blame.LOGGER.log(Level.ERROR, "\n****************** Blame Extra Info Report " + Blame.VERSION + " ******************" +
							"\n   Condensed Dispenser message mode activated for " + entry.modID +
							"\n   Dispenser behavior registry replacement was detected." +
							"\n   Reason for the change: " + entry.reasonForBehaviorChange +
							"\n   The kinds of items affected: " + entry.summaryOfItemsAffected +
							"\n   Ignore this log entry unless block behaviors aren't working with Dispensers for some reason.\n");
				}
				entry.itemBehaviorsReplaced++;
			}
			else if (!startupIgnore && (itemID.getNamespace().equals("minecraft") || this.containsKey(item))) {
				List<StackTraceElement> stackList = new ArrayList<>();
				StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
				stackList.add(stacktrace[3]);
				stackList.add(stacktrace[4]);
				stackList.add(stacktrace[5]);

				Blame.LOGGER.log(Level.ERROR, "\n****************** Blame Extra Info Report " + Blame.VERSION + " ******************" +
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


	private static class MessageCondenserEntry{
		private int itemBehaviorsReplaced = 0;
		private final String modID;
		private final String summaryOfItemsAffected;
		private final String reasonForBehaviorChange;

		public MessageCondenserEntry(String modID, String summaryOfItemsAffected, String reasonForBehaviorChange){
			this.modID = modID;
			this.summaryOfItemsAffected = summaryOfItemsAffected;
			this.reasonForBehaviorChange = reasonForBehaviorChange;
		}
	}
}
