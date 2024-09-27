package com.bobvarioa.mobitems.blocks.entities;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.blocks.MobColosseum;
import com.bobvarioa.mobitems.blocks.attachments.ItemHandlerAttachment;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob;
import com.bobvarioa.mobitems.entity.simulator.SimulatedMob.DeathResult;
import com.bobvarioa.mobitems.helpers.ItemHandlerProxy;
import com.bobvarioa.mobitems.register.ModAttachments;
import com.bobvarioa.mobitems.register.ModBlockEntities;
import com.bobvarioa.mobitems.register.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
public class MobColosseumEntity extends BlockEntity {
	public static class MobColosseumItemHandler extends ItemHandlerAttachment {
		public MobColosseumItemHandler(int i, IAttachmentHolder attachmentHolder) {
			super(i, attachmentHolder, ModAttachments.MOB_COLOSSEUM.get());
		}

		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			return switch (slot) {
				case 0, 1 -> stack.is(ModItems.MOB_ITEM.get());
				default -> true;
			};
		}

		@Override
		protected int getStackLimit(int slot, ItemStack stack) {
			return switch (slot) {
				case 0, 1 -> 1;
				default -> stack.getMaxStackSize();
			};
		}

		public List<BiConsumer<MobColosseumItemHandler, Integer>> listeners = new ArrayList<>();
		
		public void addListener(BiConsumer<MobColosseumItemHandler, Integer> consumer) {
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
			if (slot == 0 || slot == 1) {
				for (var l : listeners) {
					l.accept(this, slot);
				}
			}
		}
	}


	public MobColosseumEntity(BlockPos pos, BlockState blockState) {
		super(ModBlockEntities.MOB_COLOSSEUM.get(), pos, blockState);
	}

	public int progress = 0;
	
	public DataSlot progressSlot = new DataSlot() {
		@Override
		public int get() {
			return progress;
		}

		@Override
		public void set(int i) {
			progress = i;
		}
	};

	private boolean attacking = true;
	private Direction facing = Direction.NORTH;

	@Override
	public void onLoad() {
		super.onLoad();
		if (!level.isClientSide) {
			MobColosseumItemHandler handler = this.getData(ModAttachments.MOB_COLOSSEUM);
			handler.addListener(this::onContentsChanged);
			this.onContentsChanged(handler, 0);
			this.onContentsChanged(handler, 1);
			facing = level.getBlockState(worldPosition).getValue(MobColosseum.FACING);
		}
	}


	@SubscribeEvent
	private static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			ModBlockEntities.MOB_COLOSSEUM.get(),
			(be, side) -> {
				final MobColosseumItemHandler handler = be.getData(ModAttachments.MOB_COLOSSEUM.get());
				if (side == Direction.UP) {
					return new ItemHandlerProxy(handler, 0, 1);
				}
				if (side == be.facing.getCounterClockWise()) {
					return new ItemHandlerProxy(handler, 0, 0);
				}
				if (side == be.facing.getClockWise()) {
					return new ItemHandlerProxy(handler, 1,1);
				}
				return new ItemHandlerProxy(handler, 2, 16);
			}
		);
	}

	protected SimulatedMob simAttacker = null;
	protected SimulatedMob simDefender = null;
	
	public void onContentsChanged(MobColosseumItemHandler itemHandler, int slot) {
		if (slot == 0) {
			ItemStack stack = itemHandler.getStackInSlot(0);
			if (stack.isEmpty()) {
				simAttacker = null;
			} else {
				simAttacker = new SimulatedMob((ServerLevel) level, worldPosition.getCenter(), stack);
			}
		} else {
			ItemStack stack = itemHandler.getStackInSlot(1);
			if (stack.isEmpty()) {
				simDefender = null;
			} else {
				simDefender = new SimulatedMob((ServerLevel) level, worldPosition.getCenter(), stack);
			}
		}
	}
	
	public void moveDropsToSlots(MobColosseumItemHandler itemHandler, List<ItemStack> drops) {
		// move items to slots first, then drop in world above the block
		NonNullList<ItemStack> remaining = NonNullList.create();
		
		top: for (var drop : drops) {
			var stack = drop;
			for (int i = 2; i < 17; i++) {
				stack = itemHandler.insertItem(i, drop, false);
				if (stack.isEmpty()) continue top;
			}
			remaining.add(stack);
		}

		Containers.dropContents(level, worldPosition.above(), remaining);
	}

	public void fight(MobColosseumItemHandler itemHandler) {
		simAttacker.tick();
		simDefender.tick();
		
		if (attacking) {
			simAttacker.attack(simDefender);
			attacking = false;
		} else {
			simDefender.attack(simAttacker);
			attacking = true;
		}

		DeathResult attackerDeath = simAttacker.shouldDie();
		if (attackerDeath == DeathResult.DIE || attackerDeath == DeathResult.DISCARD) {
			if (attackerDeath != DeathResult.DISCARD) moveDropsToSlots(itemHandler, simAttacker.die(simDefender));
			itemHandler.setStackInSlot(0, ItemStack.EMPTY);
		} else if (simAttacker.dirty) {
			itemHandler.setStackInSlotSilent(0, simAttacker.getStack());
			simAttacker.dirty = false;
		}
		
		DeathResult defenderDeath = simDefender.shouldDie();
		if (defenderDeath == DeathResult.DIE || defenderDeath == DeathResult.DISCARD) {
			if (defenderDeath != DeathResult.DISCARD) moveDropsToSlots(itemHandler, simDefender.die(simAttacker));
			itemHandler.setStackInSlot(1, ItemStack.EMPTY);
		} else if (simDefender.dirty) {
			itemHandler.setStackInSlotSilent(1, simDefender.getStack());
			simDefender.dirty = false;
		}
	}

	public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
		if (blockEntity instanceof MobColosseumEntity be) {
			if (be.simAttacker == null || be.simDefender == null) return;
			if (level.hasNeighborSignal(pos)) return;
			be.progress++;
			if (be.progress >= 20 * 3) {
				if (!level.isClientSide) be.fight(be.getData(ModAttachments.MOB_COLOSSEUM));
				be.progress = 0;
			}
		}
	}
}
