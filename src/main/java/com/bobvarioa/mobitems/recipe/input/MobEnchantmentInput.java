package com.bobvarioa.mobitems.recipe.input;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record MobEnchantmentInput(ItemStack catalyst) implements RecipeInput {
	@Override
	public ItemStack getItem(int slot) {
		if (slot != 0) throw new IllegalArgumentException("No item for index " + slot);
		return this.catalyst();
	}

	@Override
	public int size() {
		return 1;
	}
}
