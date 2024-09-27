package com.bobvarioa.mobitems.entity.simulator.enchantments;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.register.ModBehaviors;
import org.jetbrains.annotations.Nullable;

public class RegenerationEnchantment extends SimulatedBehavior {
	public RegenerationEnchantment() {
		super(ModBehaviors.REGENERATION.getId());
	}

	@Override
	public void tick(SimulatedMob self) {
		self.setHealth(self.getHealth() + self.getMaxHealth() * 0.01);
	}
}
