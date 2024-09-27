package com.bobvarioa.mobitems.register.events;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

import java.util.function.BiConsumer;

public class RegisterItemBehaviorEvent extends Event implements IModBusEvent {
	private final BiConsumer<Item, SimulatedBehavior.Supplier<?>> registerMethod;
	public RegisterItemBehaviorEvent(BiConsumer<Item, SimulatedBehavior.Supplier<?>> registerMethod) {
		this.registerMethod = registerMethod;
	}
	
	public void register(Item item, SimulatedBehavior.Supplier<?> behavior) {
		this.registerMethod.accept(item, behavior);
	}
}
