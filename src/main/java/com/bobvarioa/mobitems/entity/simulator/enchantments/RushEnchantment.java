package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.MobStat;
import com.bobvarioa.mobitems.entity.simulator.Operation;
import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;
import org.jetbrains.annotations.Nullable;

public class RushEnchantment extends SimulatedBehavior {
	public RushEnchantment() {
		super(ModBehaviors.RUSH.getId());
	}

	@Override
	public void hit(SimulatedMob self, @Nullable SimulatedMob attacker) {
		self.stats.addTemp(MobStat.MOVEMENT_SPEED, 0.50, Operation.ADD_MULTIPLIER);
	}
}
