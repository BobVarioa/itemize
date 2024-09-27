package com.bobvarioa.mobitems.entity.simulator.effects;

import com.bobvarioa.mobitems.entity.simulator.SimulatedEffect;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModSimulatedEffects;

public class SlownessEffect extends SimulatedEffect {
	public SlownessEffect(int duration) {
		super(ModSimulatedEffects.SLOWNESS.getId(), duration);
	}

	@Override
	public void tick(SimulatedMob mob) {
		if (mob.random.nextIntBetweenInclusive(1, 4) == 1) {
			mob.applyStun(1);
		}
	}
}
