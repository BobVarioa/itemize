package com.bobvarioa.mobitems.entity.simulator.behaviors.entity;

import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMobBehavior;
import com.bobvarioa.mobitems.entity.simulator.effects.WitherEffect;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class WitherSkeletonBehavior extends SimulatedMobBehavior {
	public WitherSkeletonBehavior() {
		super(ModBehaviors.WITHER_SKELETON.getId());
	}

	@Override
	public void afterAttack(SimulatedMob self, SimulatedMob other) {
		other.applyEffect(new WitherEffect(4));
	}
}
