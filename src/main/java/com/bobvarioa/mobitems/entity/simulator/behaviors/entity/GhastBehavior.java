package com.bobvarioa.mobitems.entity.simulator.behaviors.entity;

import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMobBehavior;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class GhastBehavior extends SimulatedMobBehavior {
	public GhastBehavior() {
		super(ModBehaviors.GHAST.getId());
	}

	private int counter = 0;
	
	@Override
	public boolean beforeAttack(SimulatedMob self, SimulatedMob other) {
		if (counter == 3) {
			other.hit(self, 25 + self.getDamage());
			counter = 0;
		}
		counter++;
		return true;
	}
}
