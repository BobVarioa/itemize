package com.bobvarioa.mobitems.entity.simulator.effects;

import com.bobvarioa.mobitems.entity.simulator.SimulatedEffect;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModSimulatedEffects;

public class WitherEffect extends SimulatedEffect {
	public WitherEffect(int duration) {
		super(ModSimulatedEffects.WITHER.getId(), duration);
	}

	private boolean shouldWither = false;

	@Override
	public void tick(SimulatedMob mob) {
		if (shouldWither) {
			mob.hit(null, 1);
			shouldWither = false;
		} else {
			shouldWither = true;
		}
	}
}
