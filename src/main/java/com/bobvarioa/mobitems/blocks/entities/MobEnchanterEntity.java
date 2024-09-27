package com.bobvarioa.mobitems.blocks.entities;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.blocks.MobEnchanter;
import com.bobvarioa.mobitems.blocks.attachments.ItemHandlerAttachment;
import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.helpers.ItemHandlerProxy;
import com.bobvarioa.mobitems.items.components.MobEnchantmentList;
import com.bobvarioa.mobitems.recipe.MobEnchantmentRecipe;
import com.bobvarioa.mobitems.recipe.input.MobEnchantmentInput;
import com.bobvarioa.mobitems.register.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
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
import java.util.Optional;

@EventBusSubscriber(modid = MobItems.MODID, bus = EventBusSubscriber.Bus.MOD)
public class MobEnchanterEntity extends BlockEntity {
	public MobEnchanterEntity(BlockPos pos, BlockState blockState) {
		super(ModBlockEntities.MOB_ENCHANTER.get(), pos, blockState);
	}

	public static class MobEnchanterItemHandler extends ItemHandlerAttachment {
		public MobEnchanterItemHandler(int i, IAttachmentHolder attachmentHolder) {
			super(i, attachmentHolder, ModAttachments.MOB_ENCHANTER.get());
		}

		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			if (slot == 0) return stack.is(ModItems.MOB_ITEM.get());
			return true;
		}

		@Override
		protected int getStackLimit(int slot, ItemStack stack) {
			return 1;
		}
	}

	public void enchant(boolean automatic) {
		if (!level.isClientSide) {
			var itemHandler = this.getData(ModAttachments.MOB_ENCHANTER.get());
			RecipeManager recipes = level.getRecipeManager();

			ItemStack mob = itemHandler.getStackInSlot(0).copy();
			var oldEnchants = mob.get(ModDataComponents.MOB_ENCHANTMENTS.get());
			List<Holder<SimulatedBehavior.Supplier<?>>> enchants = new ArrayList<>();
			if (oldEnchants != null) {
				enchants.addAll(oldEnchants.enchantments());
			}
			while (enchants.size() < 3) {
				enchants.add(ModBehaviors.EMPTY.getDelegate());
			}
			boolean changed = false;
			for (int i = 1; i <= 3; i++) {
				var input = new MobEnchantmentInput(itemHandler.getStackInSlot(i));
				Optional<RecipeHolder<MobEnchantmentRecipe>> recipe = recipes.getRecipeFor(ModRecipeTypes.MOB_ENCHANTMENT.get(), input, level);
				if (recipe.isPresent()) {
					enchants.set(i - 1, recipe.get().value().enchantment());
					itemHandler.setStackInSlot(i, ItemStack.EMPTY);
					changed = true;
					
				}
			}
			
			if (changed) {
				mob.set(ModDataComponents.MOB_ENCHANTMENTS, new MobEnchantmentList(enchants));
				itemHandler.setStackInSlot(0, mob);
				
				if (!automatic) level.playSound(null, worldPosition, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
			}
		}
	}

	@SubscribeEvent
	private static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			ModBlockEntities.MOB_ENCHANTER.get(),
			(be, side) -> {
				final var handler = be.getData(ModAttachments.MOB_ENCHANTER.get());
				final var facing = be.getBlockState().getValue(MobEnchanter.FACING);
				if (side == Direction.UP || side == Direction.DOWN) {
					return new ItemHandlerProxy(handler, 0, 0);
				}
				if (side == facing.getCounterClockWise()) {
					return new ItemHandlerProxy(handler, 1, 1);
				}
				if (side == facing.getClockWise()) {
					return new ItemHandlerProxy(handler, 3, 3);
				}
				if (side == facing) {
					return new ItemHandlerProxy(handler, 2, 2);
				}
				return switch (side) { default -> null; };
			}
		);
	}
}
