package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.MobStat;
import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class AnimaConduitEnchantment extends SimulatedBehavior {
	public AnimaConduitEnchantment() {
		super(ModBehaviors.ANIMA_CONDUIT.getId());
	}
	
	@Override
	public boolean beforeAttack(SimulatedMob self, SimulatedMob other) {
		if (self.random.nextIntBetweenInclusive(1, 20) == 1) {
			self.setHealth(self.getHealth() + self.getTotalSoul() / 5);
			return false;
		}

		return true;
	}
}
