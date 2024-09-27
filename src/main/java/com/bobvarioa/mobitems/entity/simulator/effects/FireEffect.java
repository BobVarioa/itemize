package com.bobvarioa.mobitems.entity.simulator.effects;

import com.bobvarioa.mobitems.entity.simulator.SimulatedEffect;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModSimulatedEffects;
import net.minecraft.nbt.CompoundTag;

public class FireEffect extends SimulatedEffect {
	public FireEffect(int duration) {
		super(ModSimulatedEffects.FIRE.getId(), duration);
	}
	@Override
	public void tick(SimulatedMob mob) {
		mob.hit(null, 2);
	}

	@Override
	public void saveIntoEntity(CompoundTag tag) {
		tag.putShort("Fire", (short) (duration * 20));
	}
}
