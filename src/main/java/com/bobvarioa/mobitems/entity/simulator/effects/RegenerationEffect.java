package com.bobvarioa.mobitems.entity.simulator.effects;

import com.bobvarioa.mobitems.entity.simulator.SimulatedEffect;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModSimulatedEffects;

public class RegenerationEffect extends SimulatedEffect {
	public RegenerationEffect(int duration) {
		super(ModSimulatedEffects.REGENERATION.getId(), duration);
	}
	
	@Override
	public void tick(SimulatedMob mob) {
		// this is roughly equivalent to vanilla's regeneration II
		mob.setHealth(mob.getHealth() + 1);
	}
}
