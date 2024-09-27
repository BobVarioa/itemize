package com.bobvarioa.mobitems.gui.menu;

import com.bobvarioa.mobitems.blocks.entities.MobEnchanterEntity;
import com.bobvarioa.mobitems.blocks.entities.MobModifierEntity;
import com.bobvarioa.mobitems.gui.menu.slots.IconSlot;
import com.bobvarioa.mobitems.gui.menu.slots.MobItemSlot;
import com.bobvarioa.mobitems.register.ModAttachments;
import com.bobvarioa.mobitems.register.ModBlocks;
import com.bobvarioa.mobitems.register.ModItems;
import com.bobvarioa.mobitems.register.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class MobEnchanterMenu extends AbstractContainerMenu implements MenuHelpers {
	public final Inventory playerInv;
	public final ContainerLevelAccess access;
	public final IItemHandler dataInventory;

	public MobEnchanterMenu(int containerId, Inventory playerInv, IItemHandler dataInventory, ContainerLevelAccess access) {
		super(ModMenus.MOB_ENCHANTER.get(), containerId);
		this.playerInv = playerInv;
		this.access = access;
		this.dataInventory = dataInventory;

		this.addSlot(new MobItemSlot(dataInventory, 0, 19, 41));
		this.addSlot(new SlotItemHandler(dataInventory, 1, 62, 62));
		this.addSlot(new SlotItemHandler(dataInventory, 2, 99, 62));
		this.addSlot(new SlotItemHandler(dataInventory, 3, 136, 62));

		createPlayerInventory(playerInv, -7);
	}

	public MobEnchanterMenu(int containerId, Inventory playerInv, MobEnchanterEntity be) {
		this(containerId, playerInv, be.getData(ModAttachments.MOB_ENCHANTER.get()), ContainerLevelAccess.create(be.getLevel(), be.getBlockPos()));
	}


	public MobEnchanterMenu(int containerId, Inventory playerInv) {
		this(containerId, playerInv, new ItemStackHandler(4), ContainerLevelAccess.NULL);
	}

	public MobEnchanterMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
		this(containerId, playerInv);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		//   - Mob Item (0)
		//   - Catalysts (1-3)
		//   - Player Inventory (4 - 4+PLAYER_INVENTORY-1)
		//   - Player Hotbar (4+PLAYER_INVENTORY - 4+PLAYER_INVENTORY+PLAYER_HOTBAR-1)
		final int PLAYER_INVENTORY = 27;
		final int PLAYER_HOTBAR = 9;
		final int SLOTS = 4;

		ItemStack quickMovedStack = ItemStack.EMPTY;
		Slot quickMovedSlot = this.slots.get(index);

		if (quickMovedSlot != null && quickMovedSlot.hasItem()) {
			ItemStack rawStack = quickMovedSlot.getItem();
			quickMovedStack = rawStack.copy();

			// quick move from inventory or hotbar
			if (index >= SLOTS && index < SLOTS + PLAYER_INVENTORY + PLAYER_HOTBAR) {
				boolean moved = false;

				ItemStack mobStack = this.slots.get(0).getItem();
				if (rawStack.is(ModItems.MOB_ITEM) && mobStack.isEmpty()) {
					moved = this.moveItemStackTo(rawStack.split(1), 0, 1, false);
					quickMovedStack = rawStack.copy();
				}

				if (!moved && !mobStack.isEmpty()) {
					if (!moved) {
						moved = this.moveItemStackTo(rawStack, 1, 2, false);
					}
					if (!moved) {
						moved = this.moveItemStackTo(rawStack, 2, 3, false);
					}
					if (!moved) {
						moved = this.moveItemStackTo(rawStack, 3, 4, false);
					}
				}

				// try to move to mob slot 
				if (!moved) {
					// try to move from inventory into hotbar
					if (index < (SLOTS + PLAYER_INVENTORY)) {
						if (!this.moveItemStackTo(rawStack, SLOTS + PLAYER_INVENTORY, SLOTS + PLAYER_INVENTORY + PLAYER_HOTBAR, false)) {
							// failed, cancel quick move
							return ItemStack.EMPTY;
						}
					}
					// try to move from hotbar into inventory
					else if (!this.moveItemStackTo(rawStack, SLOTS, SLOTS + PLAYER_INVENTORY, false)) {
						// failed, cancel quick move
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.moveItemStackTo(rawStack, SLOTS, SLOTS + PLAYER_INVENTORY + PLAYER_HOTBAR, false)) {
				return ItemStack.EMPTY;
			}


			if (rawStack.isEmpty()) {
				quickMovedSlot.set(ItemStack.EMPTY);
			} else {
				quickMovedSlot.setChanged();
			}

			if (rawStack.getCount() == quickMovedStack.getCount()) {
				return ItemStack.EMPTY;
			}
			quickMovedSlot.onTake(player, rawStack);
		}

		return quickMovedStack;
	}

	@Override
	public boolean stillValid(Player player) {
		return AbstractContainerMenu.stillValid(access, player, ModBlocks.MOB_ENCHANTER.get());
	}

	@Override
	public Slot addSlot(Slot slot) {
		// just so the helper works
		return super.addSlot(slot);
	}
}
