package com.bobvarioa.mobitems.gui.menu.slots;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class LootItemSlot extends SlotItemHandler {
	public LootItemSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPickup(Player playerIn) {
		return true;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}
}
