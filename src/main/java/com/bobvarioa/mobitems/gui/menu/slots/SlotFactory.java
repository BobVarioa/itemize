package com.bobvarioa.mobitems.gui.menu.slots;

import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.items.IItemHandler;

public interface SlotFactory {
	Slot create(IItemHandler itemHandler, int index, int xPosition, int yPosition);
}
