package com.bobvarioa.mobitems.entity.simulator.behaviors.entity;

import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMobBehavior;
import com.bobvarioa.mobitems.entity.simulator.effects.FireEffect;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class BlazeBehavior extends SimulatedMobBehavior {
	public BlazeBehavior() {
		super(ModBehaviors.BLAZE.getId());
	}

	@Override
	public void afterAttack(SimulatedMob self, SimulatedMob other) {
		other.applyEffect(new FireEffect(3));
	}
}
