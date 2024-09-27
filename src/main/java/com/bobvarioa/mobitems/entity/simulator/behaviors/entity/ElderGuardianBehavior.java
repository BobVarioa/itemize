package com.bobvarioa.mobitems.entity.simulator.behaviors.entity;

import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMobBehavior;
import com.bobvarioa.mobitems.register.ModBehaviors;
import org.jetbrains.annotations.Nullable;

public class ElderGuardianBehavior extends SimulatedMobBehavior {
	public ElderGuardianBehavior() {
		super(ModBehaviors.ELDER_GUARDIAN.getId());
	}


	@Override
	public void hit(SimulatedMob self, @Nullable SimulatedMob attacker) {
		// vanilla is still 2, but comeon, 4 just feels right
		if (attacker != null) attacker.hit(self, 4);
	}
}
