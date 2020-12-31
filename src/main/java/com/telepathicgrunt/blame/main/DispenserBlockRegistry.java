package com.telepathicgrunt.blame.main;

import com.telepathicgrunt.blame.Blame;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

import static it.unimi.dsi.fastutil.HashCommon.arraySize;

/* @author - TelepathicGrunt
 *
 * Allows us to find which mod is overriding dispenser behaviors
 *
 * LGPLv3
 */
public class DispenserBlockRegistry<K, V> extends Object2ObjectOpenHashMap<K, V>{

	// Turn on registry replacement detection only after startup's putAll I do is done.
	public Boolean startupIgnore = true;
	private boolean didNotPrintQuarkEdgeCase = true;

	@Override
	public V put(final K item, final V behavior) {

		// Can't use Forge Item Registry as it is null when this method is first called by vanilla.
		// Have to check for default air as that is the default value if no entry is found for the item.
		// Getting the optional RegistryKey always return null even for values that exists. Wth Mojang?
		if(!Registry.ITEM.getKey((Item)item).toString().equals("minecraft:air")){
			ResourceLocation itemRl = Registry.ITEM.getKey((Item)item);
			StackTraceElement stack3 = Thread.currentThread().getStackTrace()[5];

			// Prevent Quark's BlockBehaviour stuff from triggering Blame and printing out 7000 lines of registry replacements that they do.
			if(stack3.getClassName().equals("vazkii.quark.content.automation.module.DispensersPlaceBlocksModule") && stack3.getMethodName().equals("loadComplete")){
				if(didNotPrintQuarkEdgeCase){
					Blame.LOGGER.log(Level.ERROR, "\n****************** Blame Extra Info Report " + Blame.VERSION + " ******************" +
							"\n   Detected Quark registry replacing the Dispenser behavior of all blocks." +
							"\n   This is part of their DispensersPlaceBlocksModule which has config options." +
							"\n   Ignore this log entry unless block behaviors aren't working with Dispensers for some reason.\n");
							didNotPrintQuarkEdgeCase = false;
				}
			}
			else if(!startupIgnore && (itemRl.getNamespace().equals("minecraft") || this.containsKey(item))){
				StackTraceElement stack = Thread.currentThread().getStackTrace()[3];
				StackTraceElement stack2 = Thread.currentThread().getStackTrace()[4];
				Blame.LOGGER.log(Level.ERROR, "\n****************** Blame Extra Info Report " + Blame.VERSION + " ******************" +
						"\n   Ignore this unless item behavior aren't working with Dispensers. If Dispenser behavior" +
						"\n   is broken, check out \"Potentially Dangerous alternative prefix `minecraft`\" lines for" +
						"\n   the item too as registry replacements might break dispenser behaviors as well." +
						"\n  Dispenser Behavior overridden for " + itemRl.toString() +
						"\n  New behavior: " + behavior.getClass().getName() +
						"\n  Old behavior: " + this.get(item).getClass().getName() +
						"\n  Registration done at: " +
						"\n    " + stack.toString() +
						"\n    " + stack2.toString() +
						"\n    " + stack3.toString() + "\n");
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

}
