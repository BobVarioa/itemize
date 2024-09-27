package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.*;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class KnockbackEnchantment extends SimulatedBehavior {
	public KnockbackEnchantment() {
		super(ModBehaviors.KNOCKBACK.getId());
	}

	@Override
	public void init(SimulatedMob self) {
		self.stats.add(MobStat.KNOCKBACK, 0.5, Operation.ADD_BASE);
	}
}
