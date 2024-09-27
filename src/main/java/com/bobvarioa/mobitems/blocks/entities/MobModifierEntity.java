package com.bobvarioa.mobitems.blocks.entities;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.blocks.attachments.ItemHandlerAttachment;
import com.bobvarioa.mobitems.helpers.ItemHandlerProxy;
import com.bobvarioa.mobitems.helpers.ItemUtils;
import com.bobvarioa.mobitems.items.MobItem;
import com.bobvarioa.mobitems.register.ModAttachments;
import com.bobvarioa.mobitems.register.ModBlockEntities;
import com.bobvarioa.mobitems.register.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@EventBusSubscriber(modid = MobItems.MODID, bus = EventBusSubscriber.Bus.MOD)
public class MobModifierEntity extends BlockEntity {
	public static class MobModifierItemHandler extends ItemHandlerAttachment {
		public MobModifierItemHandler(int i, IAttachmentHolder attachmentHolder) {
			super(i, attachmentHolder, ModAttachments.MOB_MODIFIER.get());
		}

		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			if (slot != 0 && this.getStackInSlot(0).isEmpty()) return false;
			final Equipable equipable = Equipable.get(stack);
			return switch (slot) {
				case 0 -> stack.is(ModItems.MOB_ITEM.get());
				case 1 -> equipable != null && equipable.getEquipmentSlot() == EquipmentSlot.HEAD;
				case 2 -> equipable != null && equipable.getEquipmentSlot() == EquipmentSlot.CHEST;
				case 3 -> equipable != null && equipable.getEquipmentSlot() == EquipmentSlot.LEGS;
				case 4 -> equipable != null && equipable.getEquipmentSlot() == EquipmentSlot.FEET;
				default -> true;
			};
		}

		@Override
		protected int getStackLimit(int slot, ItemStack stack) {
			return switch (slot) {
				case 0 -> 1;
				default -> stack.getMaxStackSize();
			};
		}

		public List<BiConsumer<MobModifierItemHandler, Integer>> listeners = new ArrayList<>();

		public void addListener(BiConsumer<MobModifierItemHandler, Integer> consumer) {
			listeners.add(consumer);
		}

		public void setStackInSlotSilent(int slot, ItemStack stack) {
			this.validateSlotIndex(slot);
			this.stacks.set(slot, stack);
			this.oldContentsChanged(slot);
		}

		protected void oldContentsChanged(int slot) {
			super.onContentsChanged(slot);
		}

		@Override
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);
			for (var l : listeners) {
				l.accept(this, slot);
			}
		}
	}


	public MobModifierEntity(BlockPos pos, BlockState blockState) {
		super(ModBlockEntities.MOB_MODIFIER.get(), pos, blockState);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		if (!level.isClientSide) {
			var handler = this.getData(ModAttachments.MOB_MODIFIER);
			handler.addListener(this::onContentsChanged);
		}
	}


	@SubscribeEvent
	private static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			ModBlockEntities.MOB_MODIFIER.get(),
			(be, side) -> {
				final MobModifierItemHandler handler = be.getData(ModAttachments.MOB_MODIFIER.get());
				if (side == Direction.UP || side == Direction.DOWN) {
					return new ItemHandlerProxy(handler, 0, 0);
				}
				return new ItemHandlerProxy(handler, 1, 6);
			}
		);
	}

	public void equipOnMob(MobModifierItemHandler itemHandler, EquipmentSlot slot, ItemStack stack) {
		HolderLookup.Provider lookupProvider = level.registryAccess();
		ItemStack mobStack = itemHandler.getStackInSlot(0);
		CompoundTag tag = MobItem.getEntityData(mobStack);
		switch (slot) {
			case MAINHAND, OFFHAND -> {
				tag.getList("HandItems", CompoundTag.TAG_COMPOUND).set(slot.getIndex(), stack.isEmpty() ? new CompoundTag() : stack.save(lookupProvider));
				tag.getList("HandDropChances", CompoundTag.TAG_FLOAT).set(slot.getIndex(), FloatTag.valueOf(stack.isEmpty() ? Mob.DEFAULT_EQUIPMENT_DROP_CHANCE : 2.0f));
			}
			case HEAD, CHEST, LEGS, FEET -> {
				tag.getList("ArmorItems", CompoundTag.TAG_COMPOUND).set(slot.getIndex(), stack.isEmpty() ? new CompoundTag() : stack.save(lookupProvider));
				tag.getList("ArmorDropChances", CompoundTag.TAG_FLOAT).set(slot.getIndex(), FloatTag.valueOf(2.0f));
			}
			default -> throw new RuntimeException("Unexpected equipment slot");
		}
		mobStack.set(DataComponents.ENTITY_DATA, CustomData.of(tag));
		itemHandler.setStackInSlotSilent(0, mobStack);
	}

	public void onContentsChanged(MobModifierItemHandler itemHandler, int slot) {
		switch (slot) {
			case 0 -> {
				if (itemHandler.getStackInSlot(0).isEmpty()) {
					itemHandler.setStackInSlotSilent(1, ItemStack.EMPTY);
					itemHandler.setStackInSlotSilent(2, ItemStack.EMPTY);
					itemHandler.setStackInSlotSilent(3, ItemStack.EMPTY);
					itemHandler.setStackInSlotSilent(4, ItemStack.EMPTY);
					itemHandler.setStackInSlotSilent(5, ItemStack.EMPTY);
					itemHandler.setStackInSlotSilent(6, ItemStack.EMPTY);
				} else {
					HolderLookup.Provider lookupProvider = level.registryAccess();
					CompoundTag tag = MobItem.getEntityData(itemHandler.getStackInSlot(0));
					itemHandler.setStackInSlotSilent(1, ItemUtils.parseItemStack(lookupProvider, tag.getList("ArmorItems", CompoundTag.TAG_COMPOUND).getCompound(3)));
					itemHandler.setStackInSlotSilent(2, ItemUtils.parseItemStack(lookupProvider, tag.getList("ArmorItems", CompoundTag.TAG_COMPOUND).getCompound(2)));
					itemHandler.setStackInSlotSilent(3, ItemUtils.parseItemStack(lookupProvider, tag.getList("ArmorItems", CompoundTag.TAG_COMPOUND).getCompound(1)));
					itemHandler.setStackInSlotSilent(4, ItemUtils.parseItemStack(lookupProvider, tag.getList("ArmorItems", CompoundTag.TAG_COMPOUND).getCompound(0)));
					itemHandler.setStackInSlotSilent(5, ItemUtils.parseItemStack(lookupProvider, tag.getList("HandItems", CompoundTag.TAG_COMPOUND).getCompound(0)));
					itemHandler.setStackInSlotSilent(6, ItemUtils.parseItemStack(lookupProvider, tag.getList("HandItems", CompoundTag.TAG_COMPOUND).getCompound(1)));
				}
			}

			case 1 -> equipOnMob(itemHandler, EquipmentSlot.HEAD, itemHandler.getStackInSlot(slot));
			case 2 -> equipOnMob(itemHandler, EquipmentSlot.CHEST, itemHandler.getStackInSlot(slot));
			case 3 -> equipOnMob(itemHandler, EquipmentSlot.LEGS, itemHandler.getStackInSlot(slot));
			case 4 -> equipOnMob(itemHandler, EquipmentSlot.FEET, itemHandler.getStackInSlot(slot));
			case 5 -> equipOnMob(itemHandler, EquipmentSlot.MAINHAND, itemHandler.getStackInSlot(slot));
			case 6 -> equipOnMob(itemHandler, EquipmentSlot.OFFHAND, itemHandler.getStackInSlot(slot));

			default -> throw new RuntimeException("Mob Modifier has more slots than expected");
		}
	}
}
