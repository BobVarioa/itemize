package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class StunningEnchantment extends SimulatedBehavior {
	public StunningEnchantment() {
		super(ModBehaviors.STUNNING.getId());
	}

	@Override
	public boolean beforeAttack(SimulatedMob self, SimulatedMob other) {
		if (self.random.nextIntBetweenInclusive(1, 10) == 1) {
			other.applyStun(2);
			return false;
		}

		return true;
	}
}
