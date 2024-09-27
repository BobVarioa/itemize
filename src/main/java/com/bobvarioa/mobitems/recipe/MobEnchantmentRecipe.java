package com.bobvarioa.mobitems.recipe;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.recipe.input.MobEnchantmentInput;
import com.bobvarioa.mobitems.register.ModRecipeSerializers;
import com.bobvarioa.mobitems.register.ModRecipeTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record MobEnchantmentRecipe(Ingredient catalyst, Holder<SimulatedBehavior.Supplier<?>> enchantment) implements Recipe<MobEnchantmentInput> {
	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> list = NonNullList.create();
		list.add(catalyst);
		return list;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 1;
	}

	@Override
	public boolean matches(MobEnchantmentInput input, Level level) {
		return this.catalyst.test(input.catalyst());
	}

	@Override
	public ItemStack assemble(MobEnchantmentInput input, HolderLookup.Provider provider) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider provider) {
		return ItemStack.EMPTY;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.MOB_ENCHANTMENT.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.MOB_ENCHANTMENT.get();
	}
}
