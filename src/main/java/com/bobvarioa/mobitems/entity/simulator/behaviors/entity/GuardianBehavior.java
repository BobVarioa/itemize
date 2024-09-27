package com.bobvarioa.mobitems.entity.simulator.behaviors.entity;

import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMobBehavior;
import com.bobvarioa.mobitems.register.ModBehaviors;
import org.jetbrains.annotations.Nullable;

public class GuardianBehavior extends SimulatedMobBehavior {
	public GuardianBehavior() {
		super(ModBehaviors.GUARDIAN.getId());
	}

	@Override
	public void hit(SimulatedMob self, @Nullable SimulatedMob attacker) {
		if (attacker != null) attacker.hit(self, 2);
	}
}
