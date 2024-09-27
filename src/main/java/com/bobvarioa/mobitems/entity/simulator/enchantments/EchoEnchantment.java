package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.*;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class EchoEnchantment extends SimulatedBehavior {
	public EchoEnchantment() {
		super(ModBehaviors.ECHO.getId());
	}

	@Override
	public void init(SimulatedMob self) {
		self.stats.add(MobStat.ATTACK_SPEED, 2, Operation.TOTAL_MULTIPLIER);
	}
}
