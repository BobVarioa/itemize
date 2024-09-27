package com.bobvarioa.mobitems.items.components;

import com.bobvarioa.mobitems.entity.simulator.SimulatedEffect;
import com.bobvarioa.mobitems.register.RegistryHelper;
import com.bobvarioa.mobitems.register.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public record SimulatedEffectMap(Object2IntOpenHashMap<Holder<SimulatedEffect.Factory<?>>> effects) {
	private static final Codec<Object2IntOpenHashMap<Holder<SimulatedEffect.Factory<?>>>> EFFECTS_CODEC = Codec.unboundedMap(RegistryFixedCodec.create(ModRegistries.SIMULATED_EFFECTS), Codec.INT).xmap(Object2IntOpenHashMap::new, Function.identity());
	public static final Codec<SimulatedEffectMap> CODEC = RecordCodecBuilder.create(inst -> inst.group(
		EFFECTS_CODEC.fieldOf("effects").forGetter(SimulatedEffectMap::effects)
	).apply(inst, SimulatedEffectMap::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, SimulatedEffectMap> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.map(Object2IntOpenHashMap::new, ByteBufCodecs.holderRegistry(ModRegistries.SIMULATED_EFFECTS), ByteBufCodecs.INT),
		SimulatedEffectMap::effects,
		SimulatedEffectMap::new
	);

	public static SimulatedEffectMap empty() {
		return new SimulatedEffectMap(new Object2IntOpenHashMap<>());
	}

	public static SimulatedEffectMap from(Iterable<SimulatedEffect> effectList) {
		var map = new Object2IntOpenHashMap<Holder<SimulatedEffect.Factory<?>>>();
		for (var e : effectList) {
			var effect = RegistryHelper.getMobEffect(e.id());
			map.addTo(effect, e.duration);
		}
		return new SimulatedEffectMap(map);
	}

	public List<SimulatedEffect> toList() {
		List<SimulatedEffect> list = new ArrayList<>();
		effects.forEach((k, v) -> {
			list.add(k.value().create(v));
		});
		return list;
	}
}
