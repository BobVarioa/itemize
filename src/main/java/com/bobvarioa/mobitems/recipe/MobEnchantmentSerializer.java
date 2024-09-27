package com.bobvarioa.mobitems.recipe;

import com.bobvarioa.mobitems.register.ModRegistries;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class MobEnchantmentSerializer implements RecipeSerializer<MobEnchantmentRecipe> {

	public static final MapCodec<MobEnchantmentRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		Ingredient.CODEC.fieldOf("catalyst").forGetter(MobEnchantmentRecipe::catalyst),
		RegistryFixedCodec.create(ModRegistries.SIMULATED_BEHAVIORS).fieldOf("enchantment").forGetter(MobEnchantmentRecipe::enchantment)
	).apply(inst, MobEnchantmentRecipe::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, MobEnchantmentRecipe> STREAM_CODEC =
		StreamCodec.composite(
			Ingredient.CONTENTS_STREAM_CODEC, MobEnchantmentRecipe::catalyst,
			ByteBufCodecs.holderRegistry(ModRegistries.SIMULATED_BEHAVIORS), MobEnchantmentRecipe::enchantment,
			MobEnchantmentRecipe::new
		);


	@Override
	public MapCodec<MobEnchantmentRecipe> codec() {
		return CODEC;
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, MobEnchantmentRecipe> streamCodec() {
		return STREAM_CODEC;
	}
}
