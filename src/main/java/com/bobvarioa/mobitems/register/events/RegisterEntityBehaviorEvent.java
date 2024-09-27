package com.bobvarioa.mobitems.register.events;

import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

import java.util.function.BiConsumer;

public class RegisterEntityBehaviorEvent extends Event implements IModBusEvent {
	private final BiConsumer<EntityType<?>, SimulatedBehavior.Supplier<?>> registerMethod;
	public RegisterEntityBehaviorEvent(BiConsumer<EntityType<?>, SimulatedBehavior.Supplier<?>> registerMethod) {
		this.registerMethod = registerMethod;
	}
	
	public void register(EntityType<?> entityType, SimulatedBehavior.Supplier<?> behavior) {
		this.registerMethod.accept(entityType, behavior);
	}
}
