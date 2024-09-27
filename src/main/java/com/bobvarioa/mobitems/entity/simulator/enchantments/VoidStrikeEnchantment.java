package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class VoidStrikeEnchantment extends SimulatedBehavior {
	public VoidStrikeEnchantment() {
		super(ModBehaviors.VOID_STRIKE.getId());
	}
	
	private int attacks = 0;

	@Override
	public double attack(SimulatedMob self, SimulatedMob other, double damage) {
		double mult = 4 * (1 - Math.exp((-0.05) * attacks));
		return damage + damage * mult;
	}

	@Override
	public void afterAttack(SimulatedMob self, SimulatedMob other) {
		attacks++;
	}

	@Override
	public void kill(SimulatedMob self, SimulatedMob other) {
		attacks = 0;
	}
}
