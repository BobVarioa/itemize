package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class SoulSiphonEnchantment extends SimulatedBehavior {
	public SoulSiphonEnchantment() {
		super(ModBehaviors.SOUL_SIPHON.getId());
	}

	@Override
	public void kill(SimulatedMob self, SimulatedMob other) {
		other.giveSoul(self.random.nextIntBetweenInclusive(0, (int) Math.floor(other.getRawMaxHealth() / 2)));
	}
}
