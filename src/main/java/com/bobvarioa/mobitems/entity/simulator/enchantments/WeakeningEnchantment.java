package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.effects.WeaknessEffect;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class WeakeningEnchantment extends SimulatedBehavior {
	public WeakeningEnchantment() {
		super(ModBehaviors.WEAKENING.getId());
	}
	
	@Override
	public boolean beforeAttack(SimulatedMob self, SimulatedMob other) {
		if (self.random.nextIntBetweenInclusive(1, 10) == 1) {
			other.applyEffect(new WeaknessEffect(2));
			return false;
		}

		return true;
	}
}
