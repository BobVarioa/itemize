package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.MobStat;
import com.bobvarioa.mobitems.entity.simulator.Operation;
import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;
import org.jetbrains.annotations.Nullable;

public class RampagingEnchantment extends SimulatedBehavior {
	public RampagingEnchantment() {
		super(ModBehaviors.RAMPAGING.getId());
	}
	
	// duration is cleared once the mob leaves the battle, state is transient on purpose
	public int duration = 0;

	@Override
	public void kill(SimulatedMob self, SimulatedMob other) {
		duration = 3;
	}

	@Override
	public void tick(SimulatedMob self) {
		if (duration > 0) {
			self.stats.addTemp(MobStat.ATTACK_SPEED, 1.5, Operation.ADD_MULTIPLIER);
			duration--;
		}
	}
}
