package com.bobvarioa.mobitems.entity.simulator.effects;

import com.bobvarioa.mobitems.entity.simulator.SimulatedEffect;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModSimulatedEffects;

public class PoisonEffect extends SimulatedEffect {
	public PoisonEffect(int duration) {
		super(ModSimulatedEffects.POISON.getId(), duration);
	}


	@Override
	public void tick(SimulatedMob mob) {
		if (mob.getHealth() > 1) {
			mob.hit(null,1);
		}
	}
}
