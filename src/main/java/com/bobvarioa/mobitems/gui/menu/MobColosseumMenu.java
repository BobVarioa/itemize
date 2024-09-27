package com.bobvarioa.mobitems.gui.menu;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.blocks.entities.MobColosseumEntity;
import com.bobvarioa.mobitems.gui.menu.slots.LootItemSlot;
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
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class MobColosseumMenu extends AbstractContainerMenu implements MenuHelpers {
	public final Inventory playerInv;
	public final ContainerLevelAccess access;
	public final IItemHandler dataInventory;
	public final DataSlot progress;
	
	public MobColosseumMenu(int containerId, Inventory playerInv, IItemHandler dataInventory, ContainerLevelAccess access, DataSlot progress) {
		super(ModMenus.MOB_COLOSSEUM.get(), containerId);
		this.playerInv = playerInv;
		this.access = access;
		this.dataInventory = dataInventory;
		this.progress = progress;

		this.addDataSlot(progress);
		this.addSlot(new MobItemSlot(dataInventory, 0, 8, 73));
		this.addSlot(new MobItemSlot(dataInventory, 1, 152, 73));

		createCustomInventory(dataInventory, LootItemSlot::new, 2, 17, 5, 36, 0);

		createPlayerInventory(playerInv, 2);

	}

	public MobColosseumMenu(int containerId, Inventory playerInv, MobColosseumEntity be) {
		this(containerId, playerInv, be.getData(ModAttachments.MOB_COLOSSEUM.get()), ContainerLevelAccess.create(be.getLevel(), be.getBlockPos()), be.progressSlot);
	}


	public MobColosseumMenu(int containerId, Inventory playerInv) {
		this(containerId, playerInv, new ItemStackHandler(19), ContainerLevelAccess.NULL, DataSlot.standalone());
	}

	public MobColosseumMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
		this(containerId, playerInv);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		//   - Attack Mob (0)
		//   - Defense Mob (1)
		//   - Mob Drops (2-16)
		//   - Player Inventory (17 - 17+PLAYER_INVENTORY-1)
		//   - Player Hotbar (17+PLAYER_INVENTORY - 17+PLAYER_INVENTORY+PLAYER_HOTBAR-1)
		final int PLAYER_INVENTORY = 27;
		final int PLAYER_HOTBAR = 9;
		final int SLOTS = 17;

		ItemStack quickMovedStack = ItemStack.EMPTY;
		Slot quickMovedSlot = this.slots.get(index);

		if (quickMovedSlot != null && quickMovedSlot.hasItem()) {
			ItemStack rawStack = quickMovedSlot.getItem();
			quickMovedStack = rawStack.copy();

			// quick move from inventory or hotbar
			if (index >= SLOTS && index < SLOTS + PLAYER_INVENTORY + PLAYER_HOTBAR) {
				boolean moved = false;

				if (rawStack.is(ModItems.MOB_ITEM)) {
					if (this.slots.get(0).getItem().isEmpty()) {
						moved = this.moveItemStackTo(rawStack.copyWithCount(1), 0, 1, false);
					} else if (this.slots.get(1).getItem().isEmpty()) {
						moved = this.moveItemStackTo(rawStack.copyWithCount(1), 1, 2, false);
					}
					if (moved) {
						rawStack.shrink(1);
					}
				}

				// try to move to attack or defend
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
			} else if (!this.moveItemStackTo(rawStack, SLOTS, SLOTS+PLAYER_INVENTORY+PLAYER_HOTBAR, false)) {
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
		return AbstractContainerMenu.stillValid(access, player, ModBlocks.MOB_COLOSSEUM.get());
	}

	@Override
	public Slot addSlot(Slot slot) {
		// just so the helper works
		return super.addSlot(slot);
	}
}
