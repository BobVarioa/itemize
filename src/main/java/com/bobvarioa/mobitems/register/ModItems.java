package com.bobvarioa.mobitems.register;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.items.*;
import com.bobvarioa.mobitems.items.components.SimulatedEffectMap;
import com.bobvarioa.mobitems.items.components.MobEnchantmentList;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MobItems.MODID);
	public static final DeferredHolder<Item, ConverterItem> VACUUM = ITEMS.register("vacuum",
		() -> new ConverterItem(new Item.Properties().stacksTo(1), ConverterItem.Types.VACUUM));
	public static final DeferredHolder<Item, ConverterItem> RITUAL_TABLET = ITEMS.register("ritual_tablet",
		() -> new ConverterItem(new Item.Properties().stacksTo(1), ConverterItem.Types.NORMAL));
	public static final DeferredHolder<Item, ConverterItem> CREATIVE_POCKET_CONVERTER = ITEMS.register("creative_ritual_tablet",
		() -> new ConverterItem(new Item.Properties().stacksTo(1), ConverterItem.Types.CREATIVE));
	public static final DeferredHolder<Item, BlockItem> SPAWN_CAPTURER_ITEM = ITEMS.register("spawn_capturer",
		() -> new BlockItem(ModBlocks.SPAWN_CAPTURER.get(), new Item.Properties()));
	public static final DeferredHolder<Item, BlockItem> SOUL_INSERTER_ITEM = ITEMS.register("soul_inserter",
		() -> new BlockItem(ModBlocks.SOUL_INSERTER.get(), new Item.Properties()));
	public static final DeferredHolder<Item, BlockItem> SOUL_EXTRACTOR_ITEM = ITEMS.register("soul_extractor",
		() -> new BlockItem(ModBlocks.SOUL_EXTRACTOR.get(), new Item.Properties()));
	public static final DeferredHolder<Item, BlockItem> CONVERTER_CASING_ITEM = ITEMS.register("converter_casing",
		() -> new BlockItem(ModBlocks.CONVERTER_CASING.get(), new Item.Properties()));
	public static final DeferredHolder<Item, BlockItem> CONVERTER_ITEM = ITEMS.register("converter",
		() -> new BlockItem(ModBlocks.CONVERTER.get(), new Item.Properties()));
	public static final DeferredHolder<Item, BlockItem> MOB_COLOSSEUM = ITEMS.register("mob_colosseum",
		() -> new BlockItem(ModBlocks.MOB_COLOSSEUM.get(), new Item.Properties()));
	public static final DeferredHolder<Item, BlockItem> MOB_MODIFIER = ITEMS.register("mob_modifier",
		() -> new BlockItem(ModBlocks.MOB_MODIFIER.get(), new Item.Properties()));
	public static final DeferredHolder<Item, BlockItem> MOB_ENCHANTER = ITEMS.register("mob_enchanter",
		() -> new BlockItem(ModBlocks.MOB_ENCHANTER.get(), new Item.Properties()));
	public static final DeferredHolder<Item, WormFood> WORM_FOOD = ITEMS.register("worm_food", () ->
		new WormFood(
			new Item.Properties()
				.stacksTo(1)
				.food(
					new FoodProperties.Builder()
						.alwaysEdible()
						.usingConvertsTo(Items.BOWL)
						.nutrition(-4)
						.effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 20 * 120), 1f)
						.effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 20 * 60, 3), 1f)
						.effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 30, 3), 1f)
						.effect(() -> new MobEffectInstance(MobEffects.WITHER, 20 * 5, 3), 1f)
						.effect(() -> new MobEffectInstance(MobEffects.HUNGER, 20 * 15, 3), 1f)
						.build()

				)));
	public static final DeferredHolder<Item, DustyMobSoul> DUSTY_MOB_SOUL = ITEMS.register("dusty_mob_soul", () -> new DustyMobSoul(new Item.Properties().stacksTo(64)));
	public static final DeferredHolder<Item, MobSoul> MOB_SOUL = ITEMS.register("mob_soul", () -> new MobSoul(new Item.Properties().stacksTo(64)));
	public static final DeferredHolder<Item, MobItem> MOB_ITEM = ITEMS.register("mob_item", () -> new MobItem(
		new Item.Properties()
			.stacksTo(24)
	));

	public static void register(IEventBus bus) {
		ITEMS.register(bus);
	}
}
