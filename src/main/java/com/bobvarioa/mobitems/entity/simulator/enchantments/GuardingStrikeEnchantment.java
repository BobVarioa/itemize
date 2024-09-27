package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;
import org.jetbrains.annotations.Nullable;

public class GuardingStrikeEnchantment extends SimulatedBehavior {
	public GuardingStrikeEnchantment() {
		super(ModBehaviors.GUARDING_STRIKE.getId());
	}
	
	// duration here is cleared on mob leaving the fight, state is transient on purpose
	private int duration = 0;

	@Override
	public void kill(SimulatedMob self, SimulatedMob other) {
		duration = 3;
	}

	@Override
	public void tick(SimulatedMob self) {
		if (duration > 0) duration--;
	}

	@Override
	public double beforeHit(SimulatedMob self, @Nullable SimulatedMob attacker, double damage) {
		return duration > 0 ? damage * 0.50 : damage;
	}
}
