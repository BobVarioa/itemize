package com.bobvarioa.mobitems.entity.simulator;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public abstract class SimulatedEffect extends SimulatedBehavior {
	
	@FunctionalInterface
	public interface Factory<T extends SimulatedEffect> {
		T create(int duration);
	}
	
	/**
	 * The remaining duration of this effect in ticks, mutable
	 */
	public int duration;
	
	public SimulatedEffect(ResourceLocation id, int duration) {
		super(id);
		this.duration = duration;
	}

	/**
	 * Called whenever the simulated mob is written to an item by {@link SimulatedMob#getStack()}
	 * Not necessary for most effects
	 * @param tag The entity's nbt data
	 */
	public void saveIntoEntity(CompoundTag tag) {};
}
