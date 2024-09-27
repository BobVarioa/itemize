package com.bobvarioa.mobitems.register.events;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

import java.util.function.Consumer;

public class RegisterMobEnchantmentEvent extends Event implements IModBusEvent {
	private final Consumer<SimulatedBehavior.Supplier<?>> registerMethod;
	public RegisterMobEnchantmentEvent(Consumer<SimulatedBehavior.Supplier<?>> registerMethod) {
		this.registerMethod = registerMethod;
	}
	
	public void register(SimulatedBehavior.Supplier<?> behavior) {
		this.registerMethod.accept(behavior);
	}
}
