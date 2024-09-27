package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.*;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class SharpnessEnchantment extends SimulatedBehavior {
	public SharpnessEnchantment() {
		super(ModBehaviors.SHARPNESS.getId());
	}

	@Override
	public void init(SimulatedMob self) {
		self.stats.add(MobStat.DAMAGE, 1.10, Operation.ADD_MULTIPLIER);
	}
}
