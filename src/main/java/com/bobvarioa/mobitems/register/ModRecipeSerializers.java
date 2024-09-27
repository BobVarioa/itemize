package com.bobvarioa.mobitems.register;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.recipe.MobEnchantmentRecipe;
import com.bobvarioa.mobitems.recipe.MobEnchantmentSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeSerializers {
	private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
		DeferredRegister.create(Registries.RECIPE_SERIALIZER, MobItems.MODID);

	public static final DeferredHolder<RecipeSerializer<?>, MobEnchantmentSerializer> MOB_ENCHANTMENT =
		RECIPE_SERIALIZERS.register(
			"mob_enchanting",
			MobEnchantmentSerializer::new
		);
	
	public static void register(IEventBus bus) {
		RECIPE_SERIALIZERS.register(bus);
	}
}
