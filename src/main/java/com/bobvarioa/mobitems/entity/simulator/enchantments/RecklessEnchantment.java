package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;
import org.jetbrains.annotations.Nullable;

public class RecklessEnchantment extends SimulatedBehavior {
	public RecklessEnchantment() {
		super(ModBehaviors.RECKLESS.getId());
	}

	@Override
	public double attack(SimulatedMob self, SimulatedMob other, double damage) {
		return 1.5 * damage;
	}

	@Override
	public double beforeHit(SimulatedMob self, @Nullable SimulatedMob attacker, double damage) {
		return damage * 2.0;
	}
}
