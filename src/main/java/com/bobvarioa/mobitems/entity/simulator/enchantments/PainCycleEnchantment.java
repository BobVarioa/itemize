package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class PainCycleEnchantment extends SimulatedBehavior {
	public PainCycleEnchantment() {
		super(ModBehaviors.PAIN_CYCLE.getId());
	}

	private int stacks = 0;

	@Override
	public double attack(SimulatedMob self, SimulatedMob other, double damage) {
		if (stacks == 4) {
			stacks = 0;
			return damage * 4;
		}
		
		return damage;
	}

	@Override
	public void afterAttack(SimulatedMob self, SimulatedMob other) {
		stacks++;
		self.setHealth(self.getHealth() - self.getMaxHealth() * 0.03);
	}
}
