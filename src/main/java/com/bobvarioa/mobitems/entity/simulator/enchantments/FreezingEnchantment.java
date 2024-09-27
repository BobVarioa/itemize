package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.effects.SlownessEffect;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class FreezingEnchantment extends SimulatedBehavior {
	public FreezingEnchantment() {
		super(ModBehaviors.FREEZING.getId());
	}

	@Override
	public void afterAttack(SimulatedMob self, SimulatedMob other) {
		// todo: this should technically be a separate effect 
		other.applyEffect(new SlownessEffect(2));
	}
}
