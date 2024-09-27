package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.effects.FireEffect;
import com.bobvarioa.mobitems.register.ModBehaviors;
import com.bobvarioa.mobitems.register.ModSimulatedEffects;

public class FireAspectEnchantment extends SimulatedBehavior {
	public FireAspectEnchantment() {
		super(ModBehaviors.FIRE_ASPECT.getId());
	}

	@Override
	public void afterAttack(SimulatedMob self, SimulatedMob other) {
		other.applyEffect(new FireEffect(2));
	}
}
