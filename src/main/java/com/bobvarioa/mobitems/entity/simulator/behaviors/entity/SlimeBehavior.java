package com.bobvarioa.mobitems.entity.simulator.behaviors.entity;

import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMobBehavior;
import com.bobvarioa.mobitems.register.ModBehaviors;

public class SlimeBehavior extends SimulatedMobBehavior {
	public SlimeBehavior() {
		super(ModBehaviors.SLIME.getId());
	}

	@Override
	public SimulatedMob.DeathResult shouldDie(SimulatedMob self) {
		if (self.getHealth() <= 0) {
			int size = self.tag.getInt("Size");
			if (size > 0) {
				self.tag.putInt("Size", size - 1);
				// vanilla logic but we cram it into one guy, basically 
				int children = 2 + self.random.nextInt(3);
				int newHealth = size * size * children;
				self.setHealth(newHealth); 
				self.tag.putFloat("MaxHealth", newHealth);
				self.markDirty(); // should already be set, but just in case
				return SimulatedMob.DeathResult.LIVE;
			}
		}
		return SimulatedMob.DeathResult.PASS;
	}
	
	
}
