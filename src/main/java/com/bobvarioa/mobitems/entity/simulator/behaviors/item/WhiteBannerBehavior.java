package com.bobvarioa.mobitems.entity.simulator.behaviors.item;

import com.bobvarioa.mobitems.entity.simulator.SimulatedItemBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob.DeathResult;
import com.bobvarioa.mobitems.register.ModBehaviors;
import net.minecraft.world.Containers;

public class WhiteBannerBehavior extends SimulatedItemBehavior {
	public WhiteBannerBehavior() {
		super(ModBehaviors.WHITE_BANNER.getId());
	}

	@Override
	public DeathResult shouldDie(SimulatedMob self) {
		if (self.getHealth() <= self.getMaxHealth() * 0.25 && self.getMaxHealth() > 0) {
			Containers.dropItemStack(self.level, self.position.x, self.position.y + 1, self.position.z, self.getStack());
			return DeathResult.DISCARD;
		}
		
		return DeathResult.PASS;
	}
}
