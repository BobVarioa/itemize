package com.bobvarioa.mobitems.entity.simulator.behaviors.item;

import com.bobvarioa.mobitems.entity.simulator.SimulatedItemBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.effects.RegenerationEffect;
import com.bobvarioa.mobitems.helpers.ItemUtils;
import com.bobvarioa.mobitems.register.ModBehaviors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class TotemOfUndyingBehavior extends SimulatedItemBehavior {
	public TotemOfUndyingBehavior() {
		super(ModBehaviors.BEE.getId());
	}

	@Override
	public SimulatedMob.DeathResult shouldDie(SimulatedMob self) {
		if (self.getHealth() <= 0) {
			ListTag handItems = self.tag.getList("HandItems", Tag.TAG_COMPOUND);
			CompoundTag totem = handItems.getCompound(slot.getIndex());
			if (!totem.isEmpty()) {
				ItemStack stack = ItemUtils.parseItemStack(self.level.registryAccess(), totem);
				if (!stack.isEmpty()) {
					handItems.set(slot.getIndex(), new CompoundTag());
					// vanilla heals 19 hp + 8 temp hp
					// so, we rearrange this to be 13 initial hp then 15 regen hp
					// mainly for balance reasons 
					self.setHealth(13);
					self.applyEffect(new RegenerationEffect(15));
					return SimulatedMob.DeathResult.LIVE;
				}
			}
		}
		return SimulatedMob.DeathResult.PASS;
	}
}
