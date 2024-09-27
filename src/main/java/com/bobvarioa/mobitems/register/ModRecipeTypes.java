package com.bobvarioa.mobitems.register;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.recipe.MobEnchantmentRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeTypes {
	private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
		DeferredRegister.create(Registries.RECIPE_TYPE, MobItems.MODID);

	public static final Supplier<RecipeType<MobEnchantmentRecipe>> MOB_ENCHANTMENT =
		RECIPE_TYPES.register(
			"mob_enchantment",
			() -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MobItems.MODID, "mob_enchanting"))
		);
	
	public static void register(IEventBus bus) {
		RECIPE_TYPES.register(bus);
	}
}
