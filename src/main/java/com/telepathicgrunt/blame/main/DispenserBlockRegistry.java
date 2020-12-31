package com.telepathicgrunt.blame.main;

import com.telepathicgrunt.blame.Blame;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
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

	@Override
	public V put(final K item, final V behavior) {

		// Check null as some stuff like ArmorItem triggers Blame before item is registered
		// Have to check for default air as that is the default value if no entry is found for the item.
		// Getting the optional RegistryKey always return null even for values that exists. Wth Mojang?
		if(!Registry.ITEM.getId((Item)item).toString().equals("minecraft:air")) {
			Identifier itemID = Registry.ITEM.getId((Item) item);
			if (!startupIgnore && (itemID.getNamespace().equals("minecraft") || this.containsKey(item))) {
				StackTraceElement stack = Thread.currentThread().getStackTrace()[3];
				StackTraceElement stack2 = Thread.currentThread().getStackTrace()[4];
				StackTraceElement stack3 = Thread.currentThread().getStackTrace()[5];
				Blame.LOGGER.log(Level.ERROR, "\n****************** Blame Extra Info Report " + Blame.VERSION + " ******************" +
						"\n   Ignore this unless item behavior aren't working with Dispensers." +
						"\n  Dispenser Behavior overridden for " + itemID.toString() +
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
