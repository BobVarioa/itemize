package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.effects.PoisonEffect;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class RadianceEnchantment extends SimulatedBehavior {
	public RadianceEnchantment() {
		super(ModBehaviors.RADIANCE.getId());
	}
	
	@Override
	public boolean beforeAttack(SimulatedMob self, SimulatedMob other) {
		if (self.random.nextIntBetweenInclusive(1, 10) == 1) {
			self.setHealth(self.getHealth() + self.getRawMaxHealth() / 10);
			return false;
		}

		return true;
	}
}
