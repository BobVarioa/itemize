package com.bobvarioa.mobitems.entity.simulator.effects;

import com.bobvarioa.mobitems.entity.simulator.SimulatedEffect;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModSimulatedEffects;

public class WeaknessEffect extends SimulatedEffect {
	public WeaknessEffect(int duration) {
		super(ModSimulatedEffects.WEAKNESS.getId(), duration);
	}

	@Override
	public double attack(SimulatedMob self, SimulatedMob other, double damage) {
		return damage - 4;
	}
}
