package com.bobvarioa.mobitems.register;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.blocks.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MobItems.MODID);
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
}
