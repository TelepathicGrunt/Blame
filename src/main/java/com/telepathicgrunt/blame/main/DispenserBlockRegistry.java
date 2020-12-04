package com.telepathicgrunt.blame.main;

import com.telepathicgrunt.blame.Blame;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
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

	@Override
	public V put(final K item, final V behavior) {

		if(this.containsKey(item)){
			StackTraceElement stack = Thread.currentThread().getStackTrace()[4];
			Blame.LOGGER.log(Level.ERROR,"\n****************** Blame Extra Info Report " + Blame.VERSION + " ******************" +
					"\n   Ignore this unless item behavior aren't working with Dispensers. If Dispenser behavior" +
					"\n   is broken, check out \"Potentially Dangerous alternative prefix `minecraft`\" lines for" +
					"\n   the item too as registry replacements might break dispenser behaviors as well." +
					"\n  Dispenser Behavior overridden for " + Registry.ITEM.getKey((Item)item).toString() +
					"\n  New behavior: " + behavior.getClass().getName() +
					"\n  Old behavior: " + this.get(item).getClass().getName() +
					"\n  Registration done at: " + stack.toString());
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
