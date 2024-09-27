package com.bobvarioa.mobitems.entity.simulator.behaviors.entity;

import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMobBehavior;
import com.bobvarioa.mobitems.entity.simulator.effects.PoisonEffect;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class CaveSpiderBehavior extends SimulatedMobBehavior {
	public CaveSpiderBehavior() {
		super(ModBehaviors.CREEPER.getId());
	}

	@Override
	public void afterAttack(SimulatedMob self, SimulatedMob other) {
		other.applyEffect(new PoisonEffect(4));
	}
}
