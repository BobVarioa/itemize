package com.bobvarioa.mobitems.entity.simulator.behaviors.entity;

import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMobBehavior;
import com.bobvarioa.mobitems.entity.simulator.effects.PoisonEffect;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class BeeBehavior extends SimulatedMobBehavior {
	public BeeBehavior() {
		super(ModBehaviors.BEE.getId());
	}

	@Override
	public boolean beforeAttack(SimulatedMob self, SimulatedMob other) {
		other.applyEffect(new PoisonEffect(10));
		other.hit(self, 2);
		self.discard();
		return true;
	}
}
