package com.bobvarioa.mobitems.helpers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class ItemUtils {
	/**
	 * Silent version of {@link ItemStack#parseOptional(HolderLookup.Provider, CompoundTag)}
	 */
	public static ItemStack parseItemStack(HolderLookup.Provider lookupProvider, CompoundTag tag) {
		return ItemStack.CODEC.parse(lookupProvider.createSerializationContext(NbtOps.INSTANCE), tag).resultOrPartial().orElse(ItemStack.EMPTY);
	}
}
