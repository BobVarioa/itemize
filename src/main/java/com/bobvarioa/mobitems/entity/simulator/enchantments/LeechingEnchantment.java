package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class LeechingEnchantment extends SimulatedBehavior {
	public LeechingEnchantment() {
		super(ModBehaviors.LEECHING.getId());
	}

	@Override
	public void kill(SimulatedMob self, SimulatedMob other) {
		self.setHealth(self.getHealth() + other.getMaxHealth() / 20); 
	}
}
