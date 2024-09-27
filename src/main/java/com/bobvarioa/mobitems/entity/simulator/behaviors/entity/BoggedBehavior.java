package com.bobvarioa.mobitems.entity.simulator.behaviors.entity;

import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMobBehavior;
import com.bobvarioa.mobitems.entity.simulator.effects.PoisonEffect;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class BoggedBehavior extends SimulatedMobBehavior {
	public BoggedBehavior() {
		super(ModBehaviors.BOGGED.getId());
	}

	@Override
	public void afterAttack(SimulatedMob self, SimulatedMob other) {
		other.applyEffect(new PoisonEffect(2));
	}
}
