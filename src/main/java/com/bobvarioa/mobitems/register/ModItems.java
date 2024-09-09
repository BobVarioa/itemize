package com.bobvarioa.mobitems.register;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.items.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MobItems.MODID);
    public static final DeferredHolder<Item, ConverterItem> VACUUM  = ITEMS.register("vacuum",
            () -> new ConverterItem(new Item.Properties().stacksTo(1), ConverterItem.Types.VACUUM));
    public static final DeferredHolder<Item, ConverterItem> RITUAL_TABLET = ITEMS.register("ritual_tablet",
            () -> new ConverterItem(new Item.Properties().stacksTo(1), ConverterItem.Types.NORMAL));
    public static final DeferredHolder<Item, ConverterItem> CREATIVE_POCKET_CONVERTER  = ITEMS.register("creative_ritual_tablet",
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
    public static final DeferredHolder<Item, WormFood> WORM_FOOD = ITEMS.register("worm_food", () -> new WormFood(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, DustyMobSoul> DUSTY_MOB_SOUL = ITEMS.register("dusty_mob_soul", () -> new DustyMobSoul(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, MobSoul> MOB_SOUL = ITEMS.register("mob_soul", () -> new MobSoul(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, MobItem> MOB_ITEM = ITEMS.register("mob_item", () -> new MobItem(new Item.Properties()));
}
