package com.bobvarioa.mobitems.entity.simulator;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;

public abstract class SimulatedItemBehavior extends SimulatedBehavior {
	public EquipmentSlot slot;
	
	public SimulatedItemBehavior(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean isTransient() {
		return true;
	}
}
