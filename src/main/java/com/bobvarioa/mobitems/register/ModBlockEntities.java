package com.bobvarioa.mobitems.register;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.blocks.entities.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MobItems.MODID);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpawnCapturerEntity>> SPAWN_CAPTURER_ENTITY = BLOCK_ENTITY_TYPES.register("spawn_capturer",
            () -> BlockEntityType.Builder.of(SpawnCapturerEntity::new, ModBlocks.SPAWN_CAPTURER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SoulInserterEntity>> SOUL_INSERTER_ENTITY = BLOCK_ENTITY_TYPES.register("soul_inserter",
            () -> BlockEntityType.Builder.of(SoulInserterEntity::new, ModBlocks.SOUL_INSERTER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SoulExtractorEntity>> SOUL_EXTRACTOR_ENTITY = BLOCK_ENTITY_TYPES.register("soul_extractor",
            () -> BlockEntityType.Builder.of(SoulExtractorEntity::new, ModBlocks.SOUL_EXTRACTOR.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ConverterEntity>> CONVERTER_ENTITY = BLOCK_ENTITY_TYPES.register("mobconverter",
            () -> BlockEntityType.Builder.of(ConverterEntity::new, ModBlocks.CONVERTER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MobBlockEntity>> MOB_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("mob_block",
            () -> BlockEntityType.Builder.of(MobBlockEntity::new, ModBlocks.MOB_BLOCK.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MobColosseumEntity>> MOB_COLOSSEUM = BLOCK_ENTITY_TYPES.register("mob_colosseum",
		() -> BlockEntityType.Builder.of(MobColosseumEntity::new, ModBlocks.MOB_COLOSSEUM.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MobModifierEntity>> MOB_MODIFIER = BLOCK_ENTITY_TYPES.register("mob_modifier",
		() -> BlockEntityType.Builder.of(MobModifierEntity::new, ModBlocks.MOB_MODIFIER.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MobEnchanterEntity>> MOB_ENCHANTER = BLOCK_ENTITY_TYPES.register("mob_enchanter",
		() -> BlockEntityType.Builder.of(MobEnchanterEntity::new, ModBlocks.MOB_ENCHANTER.get()).build(null));
	
	public static void register(IEventBus bus) {
		BLOCK_ENTITY_TYPES.register(bus);
	}
}
