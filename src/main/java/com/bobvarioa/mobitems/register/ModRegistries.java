package com.bobvarioa.mobitems.register;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.entity.simulator.SimulatedEffect;
import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModRegistries {
	public static final ResourceKey<Registry<SimulatedBehavior.Supplier<?>>> SIMULATED_BEHAVIORS = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MobItems.MODID, "mob_enchantments"));
	public static final ResourceKey<Registry<SimulatedEffect.Factory<?>>> SIMULATED_EFFECTS = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MobItems.MODID, "simulated_effects"));

	@ApiStatus.Internal
	public static final Map<Item, SimulatedBehavior.Supplier<?>> ITEM_BEHAVIORS = new HashMap<>();
	@ApiStatus.Internal
	public static final Map<EntityType<?>, SimulatedBehavior.Supplier<?>> ENTITY_BEHAVIORS = new HashMap<>();
	@ApiStatus.Internal
	public static final List<SimulatedBehavior.Supplier<?>> MOB_ENCHANTMENTS = new ArrayList<>();

	public static void register(IEventBus bus) {
		bus.addListener(ModRegistries::newRegistryEvent);
	}

	private static void newRegistryEvent(NewRegistryEvent event) {
		event.create(new RegistryBuilder<>(SIMULATED_BEHAVIORS).sync(true).maxId(300));
		event.create(new RegistryBuilder<>(SIMULATED_EFFECTS).sync(true).maxId(300));
	}

}
