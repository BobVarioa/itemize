package com.bobvarioa.mobitems.register;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.entity.simulator.SimulatedEffect;
import com.bobvarioa.mobitems.entity.simulator.effects.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSimulatedEffects {
	private static final DeferredRegister<SimulatedEffect.Factory<?>> INTERNAL = DeferredRegister.create(ModRegistries.SIMULATED_EFFECTS, MobItems.MODID);

	public static final DeferredHolder<SimulatedEffect.Factory<?>, SimulatedEffect.Factory<FireEffect>> FIRE = INTERNAL.register("fire", () -> FireEffect::new);
	public static final DeferredHolder<SimulatedEffect.Factory<?>, SimulatedEffect.Factory<PoisonEffect>> POISON = INTERNAL.register("poison", () -> PoisonEffect::new);
	public static final DeferredHolder<SimulatedEffect.Factory<?>, SimulatedEffect.Factory<WitherEffect>> WITHER = INTERNAL.register("wither", () -> WitherEffect::new);
	public static final DeferredHolder<SimulatedEffect.Factory<?>, SimulatedEffect.Factory<SlownessEffect>> SLOWNESS = INTERNAL.register("slowness", () -> SlownessEffect::new);
	public static final DeferredHolder<SimulatedEffect.Factory<?>, SimulatedEffect.Factory<WeaknessEffect>> WEAKNESS = INTERNAL.register("weakness", () -> WeaknessEffect::new);
	public static final DeferredHolder<SimulatedEffect.Factory<?>, SimulatedEffect.Factory<RegenerationEffect>> REGENERATION = INTERNAL.register("regeneration", () -> RegenerationEffect::new);
	
	public static void register(IEventBus bus) {
		INTERNAL.register(bus);
	}
}
