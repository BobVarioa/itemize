package com.bobvarioa.mobitems.entity.simulator;

import net.minecraft.resources.ResourceLocation;

public abstract class SimulatedMobBehavior extends SimulatedBehavior {
	
	
	public SimulatedMobBehavior(ResourceLocation id) {
		super(id);
	}
	

	@Override
	public boolean isTransient() {
		return true;
	}
}
