package com.bobvarioa.mobitems.register;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.blocks.entities.*;
import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
	private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MobItems.MODID);
	public static final Supplier<AttachmentType<SoulInserterEntity.SoulInserterItemStackHandler>> SOUL_INSERTER = ATTACHMENT_TYPES.register("soul_inserter_handler", () ->
		AttachmentType.serializable((attachmentHolder) -> new SoulInserterEntity.SoulInserterItemStackHandler(1, attachmentHolder)).build()
	);
	public static final Supplier<AttachmentType<SoulExtractorEntity.SoulExtractorItemHandler>> SOUL_EXTRACTOR = ATTACHMENT_TYPES.register("soul_extractor_handler", () ->
		AttachmentType.serializable((attachmentHolder) -> new SoulExtractorEntity.SoulExtractorItemHandler(1, attachmentHolder)).build()
	);
	public static final Supplier<AttachmentType<MobColosseumEntity.MobColosseumItemHandler>> MOB_COLOSSEUM = ATTACHMENT_TYPES.register("mob_colosseum_handler", () ->
        AttachmentType.serializable((attachmentHolder) -> new MobColosseumEntity.MobColosseumItemHandler(17, attachmentHolder)).build()
    );
	public static final Supplier<AttachmentType<MobModifierEntity.MobModifierItemHandler>> MOB_MODIFIER = ATTACHMENT_TYPES.register("mob_modifier_handler", () ->
		AttachmentType.serializable((attachmentHolder) -> new MobModifierEntity.MobModifierItemHandler(7, attachmentHolder)).build()
	);
	public static final Supplier<AttachmentType<MobEnchanterEntity.MobEnchanterItemHandler>> MOB_ENCHANTER = ATTACHMENT_TYPES.register("mob_enchanter_handler", () ->
		AttachmentType.serializable((attachmentHolder) -> new MobEnchanterEntity.MobEnchanterItemHandler(4, attachmentHolder)).build()
	);

    public static final Supplier<AttachmentType<Float>> SOUL = ATTACHMENT_TYPES.register(
		"soul", () -> AttachmentType.builder(() -> 0f).serialize(Codec.FLOAT).build());

	public static final Supplier<AttachmentType<Float>> USED_SOUL = ATTACHMENT_TYPES.register(
		"used_soul", () -> AttachmentType.builder(() -> 0f).serialize(Codec.FLOAT).build());

	
	public static void register(IEventBus bus) {
		ATTACHMENT_TYPES.register(bus);
	}
}
