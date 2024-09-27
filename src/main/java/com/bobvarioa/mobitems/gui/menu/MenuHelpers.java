package com.bobvarioa.mobitems.gui.menu;

import com.bobvarioa.mobitems.gui.menu.slots.SlotFactory;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public interface MenuHelpers {
	Slot addSlot(Slot slot);
	default void createPlayerInventory(Inventory playerInv, int y) {
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 103 + i * 18 + y));
			}
		}

		for(int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInv, i, 8 + i * 18, 161 + y));
		}
	}
	
	default void createInventory(IItemHandler inv, int startingSlot, int endingSlot, int columns, int x, int y) {
		assert (endingSlot - startingSlot + 1) % columns == 0;
		int slot = startingSlot;
		for(int row = 0; slot < endingSlot; row++) {
			for(int column = 0; column < columns; ++column) {
				this.addSlot(new SlotItemHandler(inv, slot, 8 + column * 18 + x, 18 + row * 18 + y));
				slot++;
			}
		}
	}

	default void createCustomInventory(IItemHandler inv, SlotFactory factory, int startingSlot, int endingSlot, int columns, int x, int y) {
		assert (endingSlot - startingSlot + 1) % columns == 0;
		int slot = startingSlot;
		for(int row = 0; slot < endingSlot; row++) {
			for(int column = 0; column < columns; ++column) {
				this.addSlot(factory.create(inv, slot, 8 + column * 18 + x, 18 + row * 18 + y));
				slot++;
			}
		}
	}
}
