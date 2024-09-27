package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;
import org.jetbrains.annotations.Nullable;

public class ProtectionEnchantment extends SimulatedBehavior {
	public ProtectionEnchantment() {
		super(ModBehaviors.PROTECTION.getId());
	}

	@Override
	public double beforeHit(SimulatedMob self, @Nullable SimulatedMob attacker, double damage) {
		return damage * 0.90f;
	}
}
