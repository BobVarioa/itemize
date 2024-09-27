package com.bobvarioa.mobitems.entity.simulator;

import java.util.*;

/**
 * This is basically vanilla's {@link net.minecraft.world.entity.ai.attributes.AttributeMap}
 * But strictly for usage in {@link SimulatedMob}
 */
public class StatMap {


	private record AttributeValue(double value, Operation operation, boolean temp) {
	}

	private final Map<MobStat, Set<AttributeValue>> attrs = new EnumMap<>(MobStat.class);

	private boolean frozen = false;

	public void freeze() {
		frozen = true;
	}

	private Set<AttributeValue> getValues(MobStat stat) {
		if (attrs.containsKey(stat)) {
			return attrs.get(stat);
		}
		var list = new HashSet<AttributeValue>();
		attrs.put(stat, list);
		return list;
	}

	public double get(MobStat stat) {
		double value = 0.0;
		double multiplier = 1.0;
		double totalMultiplier = 1.0;
		var values = getValues(stat);
		for (Iterator<AttributeValue> it = values.iterator(); it.hasNext(); ) {
			var attr = it.next();
			if (attr.temp) {
				it.remove();
			}
			switch (attr.operation) {
				case ADD_BASE -> value += attr.value;
				case ADD_MULTIPLIER -> multiplier += attr.value;
				case TOTAL_MULTIPLIER -> totalMultiplier *= attr.value;
			}
		}

        return Math.max(value * multiplier * totalMultiplier, stat.min);
    }

	public void add(MobStat stat, double value, Operation operation) {
		if (frozen) throw new IllegalStateException("Cannot add a permanent attribute value while StatMap is frozen.");
		if (stat == MobStat.INVALID) return;
		getValues(stat).add(new AttributeValue(value, operation, false));
	}

	public void addTemp(MobStat stat, double value, Operation operation) {
		if (stat == MobStat.INVALID) return;
		getValues(stat).add(new AttributeValue(value, operation, true));
	}
}
