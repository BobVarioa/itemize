package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class FinalShoutEnchantment extends SimulatedBehavior {
	public FinalShoutEnchantment() {
		super(ModBehaviors.FINAL_SHOUT.getId());
	}

	@Override
	public double attack(SimulatedMob self, SimulatedMob other, double damage) {
		if (self.getHealth() <= self.getMaxHealth() * 0.25) {
			return damage * 2;
		}
			
		return damage; 
	}
}
