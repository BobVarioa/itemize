package com.bobvarioa.mobitems.register;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.items.components.MobEnchantmentList;
import com.bobvarioa.mobitems.items.components.SimulatedEffectMap;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
	private static final DeferredRegister.DataComponents REGISTRY = DeferredRegister.createDataComponents(MobItems.MODID);
	
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimulatedEffectMap>> SIMULATED_EFFECTS = REGISTRY.registerComponentType("mob_effects", 
		builder -> builder.persistent(SimulatedEffectMap.CODEC).networkSynchronized(SimulatedEffectMap.STREAM_CODEC) 
	);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<MobEnchantmentList>> MOB_ENCHANTMENTS = REGISTRY.registerComponentType("mob_enchantments",
		builder -> builder.persistent(MobEnchantmentList.CODEC).networkSynchronized(MobEnchantmentList.STREAM_CODEC)
	);
	
	public static void register(IEventBus bus) {
		REGISTRY.register(bus);
	}
}
