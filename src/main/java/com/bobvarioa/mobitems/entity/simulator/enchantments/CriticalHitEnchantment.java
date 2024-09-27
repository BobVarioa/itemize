package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.MobStat;
import com.bobvarioa.mobitems.entity.simulator.Operation;
import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class CriticalHitEnchantment extends SimulatedBehavior {
	public CriticalHitEnchantment() {
		super(ModBehaviors.CRITICAL_HIT.getId());
	}

	@Override
	public void init(SimulatedMob self) {
		self.stats.add(MobStat.CRIT_MULTIPLIER, 1.5, Operation.ADD_BASE);
		self.stats.add(MobStat.CRIT_CHANCE, 1, Operation.ADD_BASE);
	}
}
