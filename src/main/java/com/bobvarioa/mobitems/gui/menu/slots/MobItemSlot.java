package com.bobvarioa.mobitems.gui.menu.slots;

import com.bobvarioa.mobitems.MobItems;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class MobItemSlot extends SlotItemHandler {


	public static final ResourceLocation BLOCK_ATLAS = ResourceLocation.withDefaultNamespace("textures/atlas/blocks.png");
	public static final ResourceLocation EMPTY_MOB = ResourceLocation.fromNamespaceAndPath(MobItems.MODID, "item/empty_slot_mobitem");
	
	public MobItemSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Nullable
	@Override
	public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
		return Pair.of(BLOCK_ATLAS, EMPTY_MOB);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return !this.hasItem();
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}
}
