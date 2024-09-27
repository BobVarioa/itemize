package com.bobvarioa.mobitems.gui.menu;

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

public class MobModifierMenu extends AbstractContainerMenu implements MenuHelpers {
	public final Inventory playerInv;
	public final ContainerLevelAccess access;
	public final IItemHandler dataInventory;


	public static final ResourceLocation HEAD = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet");
	public static final ResourceLocation CHEST = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate");
	public static final ResourceLocation LEGS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings");
	public static final ResourceLocation FEET = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots");
	public static final ResourceLocation SHIELD = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_shield");


	public MobModifierMenu(int containerId, Inventory playerInv, IItemHandler dataInventory, ContainerLevelAccess access) {
		super(ModMenus.MOB_MODIFIER.get(), containerId);
		this.playerInv = playerInv;
		this.access = access;
		this.dataInventory = dataInventory;

		this.addSlot(new MobItemSlot(dataInventory, 0, 44, 73));
		this.addSlot(new IconSlot(HEAD, dataInventory, 1, 98, 19));
		this.addSlot(new IconSlot(CHEST, dataInventory, 2, 98, 37));
		this.addSlot(new IconSlot(LEGS, dataInventory, 3, 98, 55));
		this.addSlot(new IconSlot(FEET, dataInventory, 4, 98, 73));
		this.addSlot(new SlotItemHandler(dataInventory, 5, 80, 37));
		this.addSlot(new IconSlot(SHIELD, dataInventory, 6, 116, 37));

		createPlayerInventory(playerInv, 2);
	}

	public MobModifierMenu(int containerId, Inventory playerInv, MobModifierEntity be) {
		this(containerId, playerInv, be.getData(ModAttachments.MOB_MODIFIER.get()), ContainerLevelAccess.create(be.getLevel(), be.getBlockPos()));
	}


	public MobModifierMenu(int containerId, Inventory playerInv) {
		this(containerId, playerInv, new ItemStackHandler(7), ContainerLevelAccess.NULL);
	}

	public MobModifierMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
		this(containerId, playerInv);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		//   - Mob Item (0)
		//   - Helmet (1)
		//   - Chestplate (2)
		//   - Leggings (3)
		//   - Boots (4)
		//   - Mainhand (5)
		//   - Offhand (6)
		//   - Player Inventory (7 - 7+PLAYER_INVENTORY-1)
		//   - Player Hotbar (7+PLAYER_INVENTORY - 7+PLAYER_INVENTORY+PLAYER_HOTBAR-1)
		final int PLAYER_INVENTORY = 27;
		final int PLAYER_HOTBAR = 9;
		final int SLOTS = 7;

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
					Equipable equipable = Equipable.get(rawStack);
					if (equipable != null) {
						int slot = switch (equipable.getEquipmentSlot()) {
							case HEAD -> 1;
							case CHEST -> 2;
							case LEGS -> 3;
							case FEET -> 4;
							default -> -1;
						};
						if (slot != -1) {
							moved = this.moveItemStackTo(rawStack, slot, slot + 1, false);
						}
					}
					
					if (!moved) {
						moved = this.moveItemStackTo(rawStack, 5, 6, false);
					}

					if (!moved) {
						moved = this.moveItemStackTo(rawStack, 6, 7, false);
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
		return AbstractContainerMenu.stillValid(access, player, ModBlocks.MOB_MODIFIER.get());
	}

	@Override
	public Slot addSlot(Slot slot) {
		// just so the helper works
		return super.addSlot(slot);
	}
}
