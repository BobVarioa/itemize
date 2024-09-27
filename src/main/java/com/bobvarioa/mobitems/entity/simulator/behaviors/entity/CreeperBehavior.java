package com.bobvarioa.mobitems.entity.simulator.behaviors.entity;

import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMobBehavior;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class CreeperBehavior extends SimulatedMobBehavior {
	public CreeperBehavior() {
		super(ModBehaviors.CREEPER.getId());
	}

	private boolean shouldExplode = false;

	@Override
	public boolean beforeAttack(SimulatedMob self, SimulatedMob other) {
		if (shouldExplode) {
			if (self.tag.getBoolean("powered")) {
				other.hit(self, self.getHealth() * 4);
			} else {
				other.hit(self, self.getHealth() * 2);
			}
			self.discard();
		}
		shouldExplode = true;
		return true;
	}
}
