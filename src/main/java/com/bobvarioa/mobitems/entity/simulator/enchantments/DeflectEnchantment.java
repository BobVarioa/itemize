package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;
import org.jetbrains.annotations.Nullable;

public class DeflectEnchantment extends SimulatedBehavior {
	public DeflectEnchantment() {
		super(ModBehaviors.DEFLECT.getId());
	}

	@Override
	public double beforeHit(SimulatedMob self, @Nullable SimulatedMob attacker, double damage) {
		if (attacker != null && self.random.nextIntBetweenInclusive(1, 10) == 1) {
			double reflectionDamage = damage * 0.25;
			damage -= reflectionDamage;
			attacker.hit(null, reflectionDamage);
		}
		
		return damage;
	}
}
