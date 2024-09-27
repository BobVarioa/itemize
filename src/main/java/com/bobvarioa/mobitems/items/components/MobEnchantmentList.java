package com.bobvarioa.mobitems.items.components;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.register.RegistryHelper;
import com.bobvarioa.mobitems.register.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import it.unimi.dsi.fastutil.objects.ReferenceLists;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;

import java.util.ArrayList;
import java.util.List;

public record MobEnchantmentList(List<Holder<SimulatedBehavior.Supplier<?>>> enchantments) {
	private static final Codec<List<Holder<SimulatedBehavior.Supplier<?>>>> EFFECTS_CODEC = RegistryFixedCodec.create(ModRegistries.SIMULATED_BEHAVIORS).listOf();
	public static final Codec<MobEnchantmentList> CODEC = RecordCodecBuilder.create(inst -> inst.group(
		EFFECTS_CODEC.fieldOf("enchantments").forGetter(MobEnchantmentList::enchantments)
	).apply(inst, MobEnchantmentList::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, MobEnchantmentList> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.holderRegistry(ModRegistries.SIMULATED_BEHAVIORS).apply(ByteBufCodecs.list()),
		MobEnchantmentList::enchantments,
		MobEnchantmentList::new
	);

	public static MobEnchantmentList empty() {
		return new MobEnchantmentList(new ArrayList<>());
	}


	public static MobEnchantmentList from(Iterable<SimulatedBehavior> enchantmentList) {
		var list = new ArrayList<Holder<SimulatedBehavior.Supplier<?>>>();
		for (var e : enchantmentList) {
			if (e.isTransient()) continue;
			var enchant = RegistryHelper.getBehavior(e.id());
			if (enchant != null) {
				list.add(enchant);
			}
		}
		return new MobEnchantmentList(list);
	}

	public List<SimulatedBehavior> toList() {
		List<SimulatedBehavior> list = new ArrayList<>();
		for (var e : enchantments) {
			list.add(e.value().get());
		}
		return list;
	}
}
