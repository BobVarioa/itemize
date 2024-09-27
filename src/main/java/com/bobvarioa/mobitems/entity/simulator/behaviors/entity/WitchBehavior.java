package com.bobvarioa.mobitems.entity.simulator.behaviors.entity;

import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMobBehavior;
import com.bobvarioa.mobitems.entity.simulator.effects.SlownessEffect;
import com.bobvarioa.mobitems.entity.simulator.effects.WeaknessEffect;
import com.bobvarioa.mobitems.register.ModBehaviors;
import com.bobvarioa.mobitems.register.ModSimulatedEffects;

public class WitchBehavior extends SimulatedMobBehavior {
	public WitchBehavior() {
		super(ModBehaviors.WITCH.getId());
	}

	@Override
	public boolean beforeAttack(SimulatedMob self, SimulatedMob other) {
		// loosely based on the vanilla witch's behavior
		if (!other.hasEffect(ModSimulatedEffects.SLOWNESS.getId())) {
			other.applyEffect(new SlownessEffect(8)); // vanilla 1:30
			return true;
		}

		if (other.getHealth() > 8 && !other.hasEffect(ModSimulatedEffects.POISON.getId())) {
			other.applyEffect(new SlownessEffect(4)); // vanilla 0:45
			return true;
		}
		
		if (!other.hasEffect(ModSimulatedEffects.WEAKNESS.getId()) && self.random.nextIntBetweenInclusive(1,4) == 1) {
			other.applyEffect(new WeaknessEffect(8)); // vanilla 1:30
			return true;
		}
		
		// fire resistance?
		// swiftness?
		
		if (self.getHealth() < self.getMaxHealth() && self.random.nextIntBetweenInclusive(1,100) <= 10) { // vanilla is 5% chance
			self.setHealth(self.getHealth() + 5); // vanilla is 4
			return true;
		}
		
		other.hit(self, 6);
		return true;
	}
}
