package com.telepathicgrunt.blame.main;

import com.telepathicgrunt.blame.Blame;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

import java.util.Arrays;

import static it.unimi.dsi.fastutil.HashCommon.arraySize;

/* @author - TelepathicGrunt
 *
 * Allows us to find which mod is overriding dispenser behaviors
 *
 * LGPLv3
 */
public class DispenserBlockRegistry<K, V> extends Object2ObjectOpenHashMap<K, V>{

	@Override
	public V put(final K k, final V v) {

		if(this.containsKey(k)){
			Blame.LOGGER.log(Level.ERROR,
					"\n   BLAME extra info. Ignore this unless items aren't working with Dispensers. " +
							"\n   Also, check \"Potentially Dangerous alternative prefix `minecraft`\" lines for" +
							"\n   the item too as registry replacements might break dispenser behaviors as well." +
							"\n  Dispenser Behavior overridden for " + Registry.ITEM.getKey((Item)k).toString() +
							"\n  New behavior: " + v.getClass().getName() +
							"\n  Old behavior: " + this.get(k).getClass().getName() +
							"\n  Registration done at:");
			Thread.dumpStack();
		}

		final int pos = find(k);
		if (pos < 0) {
			insert(-pos - 1, k, v);
			return defRetValue;
		}
		final V oldValue = value[pos];
		value[pos] = v;
		return oldValue;
	}


	private int find(final K k) {
		if (((k) == null))
			return containsNullKey ? n : -(n + 1);
		K curr;
		final K[] key = this.key;
		int pos;
		// The starting point.
		if (((curr = key[pos = (it.unimi.dsi.fastutil.HashCommon.mix((k).hashCode())) & mask]) == null))
			return -(pos + 1);
		if (((k).equals(curr)))
			return pos;
		// There's always an unused entry.
		while (true) {
			if (((curr = key[pos = (pos + 1) & mask]) == null))
				return -(pos + 1);
			if (((k).equals(curr)))
				return pos;
		}
	}
	private void insert(final int pos, final K k, final V v) {
		if (pos == n)
			containsNullKey = true;
		key[pos] = k;
		value[pos] = v;
		if (size++ >= maxFill)
			rehash(arraySize(size + 1, f));
	}

}
