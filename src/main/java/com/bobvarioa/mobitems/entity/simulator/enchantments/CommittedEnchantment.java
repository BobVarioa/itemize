package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class CommittedEnchantment extends SimulatedBehavior {
	public CommittedEnchantment() {
		super(ModBehaviors.COMMITTED.getId());
	}


	@Override
	public double attack(SimulatedMob self, SimulatedMob other, double damage) {
		double mult = -1 * (1 - Math.exp((1.2) * (self.getHealth() / self.getMaxHealth())));
		return damage + damage * mult;
	}
}
