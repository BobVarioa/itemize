package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.effects.PoisonEffect;
import com.bobvarioa.mobitems.entity.simulator.effects.WeaknessEffect;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class PoisonCloudEnchantment extends SimulatedBehavior {
	public PoisonCloudEnchantment() {
		super(ModBehaviors.POISON_CLOUD.getId());
	}

	@Override
	public boolean beforeAttack(SimulatedMob self, SimulatedMob other) {
		if (self.random.nextIntBetweenInclusive(1, 10) == 1) {
			other.applyEffect(new PoisonEffect(2));
			return false;
		}

		return true;
	}
}
