package com.bobvarioa.mobitems.register;

import com.bobvarioa.mobitems.entity.simulator.SimulatedEffect;
import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.register.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class RegistryHelper {
	static Registry<SimulatedBehavior.Supplier<?>> SIMULATED_BEHAVIORS;
	public static Holder<SimulatedBehavior.Supplier<?>> getBehavior(ResourceLocation location) {
		if (SIMULATED_BEHAVIORS == null) SIMULATED_BEHAVIORS = (Registry<SimulatedBehavior.Supplier<?>>) BuiltInRegistries.REGISTRY.get(ModRegistries.SIMULATED_BEHAVIORS.location());
		if (SIMULATED_BEHAVIORS == null) return null;
		return SIMULATED_BEHAVIORS.getHolder(location).orElse(null);
	}
	
	public static ResourceLocation getBehaviorId(SimulatedBehavior.Supplier<?> behavior) {
		if (SIMULATED_BEHAVIORS == null) SIMULATED_BEHAVIORS = (Registry<SimulatedBehavior.Supplier<?>>) BuiltInRegistries.REGISTRY.get(ModRegistries.SIMULATED_BEHAVIORS.location());
		if (SIMULATED_BEHAVIORS == null) return null;
		return SIMULATED_BEHAVIORS.getKey(behavior);
	}

	static Registry<SimulatedEffect.Factory<?>> SIMULATED_EFFECTS;
	public static Holder<SimulatedEffect.Factory<?>> getMobEffect(ResourceLocation location) {
		if (SIMULATED_EFFECTS == null) SIMULATED_EFFECTS = (Registry<SimulatedEffect.Factory<?>>) BuiltInRegistries.REGISTRY.get(ModRegistries.SIMULATED_EFFECTS.location());
		if (SIMULATED_EFFECTS == null) return null;
		return SIMULATED_EFFECTS.getHolder(location).orElse(null);
	}

	public static ResourceLocation getMobEffectId(SimulatedEffect.Factory<?> effect) {
		if (SIMULATED_EFFECTS == null) SIMULATED_EFFECTS = (Registry<SimulatedEffect.Factory<?>>) BuiltInRegistries.REGISTRY.get(ModRegistries.SIMULATED_EFFECTS.location());
		if (SIMULATED_EFFECTS == null) return null;
		return SIMULATED_EFFECTS.getKey(effect);
	}
}
