package com.bobvarioa.mobitems.register;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.blocks.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.EventBus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MobItems.MODID);
	public static final DeferredHolder<Block, SpawnCapturer> SPAWN_CAPTURER = BLOCKS.register("spawn_capturer",
		() -> new SpawnCapturer(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
	public static final DeferredHolder<Block, SoulInserter> SOUL_INSERTER = BLOCKS.register("soul_inserter",
		() -> new SoulInserter(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
	public static final DeferredHolder<Block, SoulExtractor> SOUL_EXTRACTOR = BLOCKS.register("soul_extractor",
		() -> new SoulExtractor(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
	public static final DeferredHolder<Block, ConverterCasing> CONVERTER_CASING = BLOCKS.register("converter_casing",
		() -> new ConverterCasing(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
	public static final DeferredHolder<Block, Converter> CONVERTER = BLOCKS.register("converter",
		() -> new Converter(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
	public static final DeferredHolder<Block, MobBlock> MOB_BLOCK = BLOCKS.register("mob_block",
		() -> new MobBlock(Block.Properties.of().noCollission().noOcclusion().noLootTable().instabreak()));
	public static final DeferredHolder<Block, MobColosseum> MOB_COLOSSEUM = BLOCKS.register("mob_colosseum",
		() -> new MobColosseum(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
	public static final DeferredHolder<Block, MobModifier> MOB_MODIFIER = BLOCKS.register("mob_modifier",
		() -> new MobModifier(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
	
	public static final DeferredHolder<Block, MobEnchanter> MOB_ENCHANTER = BLOCKS.register("mob_enchanter",
		() -> new MobEnchanter(BlockBehaviour.Properties.of().noOcclusion().dynamicShape()));

	public static void register(IEventBus bus) {
		BLOCKS.register(bus);
	}
}
