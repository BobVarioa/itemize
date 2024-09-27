package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;
import org.jetbrains.annotations.Nullable;

public class ThornsEnchantment extends SimulatedBehavior {
	public ThornsEnchantment() {
		super(ModBehaviors.THORNS.getId());
	}

	@Override
	public double beforeHit(SimulatedMob self, @Nullable SimulatedMob attacker, double damage) {
		if (attacker != null) {
			attacker.hit(null, damage * 0.50);	
		}
		return damage;
	}
}
