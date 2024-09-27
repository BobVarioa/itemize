package com.bobvarioa.mobitems.entity.simulator;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public abstract class SimulatedBehavior {
	@FunctionalInterface
	public interface Supplier<T extends SimulatedBehavior> {
		T get();
	}

	private final ResourceLocation id;

	public SimulatedBehavior(ResourceLocation id) {
		this.id = id;
	}

	public ResourceLocation id() {
		return id;
	}

	/**
	 * Is this a transient?
	 * All being transient means is that these effects are added dynamically to the mob and shouldn't be saved
	 */
	public boolean isTransient() {
		return false;
	}

	/**
	 * Called every battle "tick", instead of game ticks, this should be treated as every 1 second, not 1/20th of a second
	 *
	 * @param self The simulated self this behavior belongs to
	 * @apiNote Technically happens every 60 game ticks, but when transposing vanilla things into this system, 1 battle tick = 1 second
	 */
	public void tick(SimulatedMob self) {
	}

	/**
	 * Called before a mob decides to attack
	 *
	 * @param self  The simulated mob this behavior belongs to
	 * @param other The simulated mob this is attacking
	 * @return should the attack be cancelled?
	 */
	public boolean beforeAttack(SimulatedMob self, SimulatedMob other) {
		return false;
	}


	/**
	 * Called after a mob decides to attack, but before any damage is done
	 *
	 * @param self   The simulated mob this behavior belongs to
	 * @param other  The simulated mob this is attacking
	 * @param damage The current damage this attack does
	 * @return The damage this attack should do
	 */
	public double attack(SimulatedMob self, SimulatedMob other, double damage) {
		return damage;
	}

	/**
	 * Called after an attack is done
	 *
	 * @param self  The simulated mob this belongs to
	 * @param other The simulated mob this is attacking
	 */
	public void afterAttack(SimulatedMob self, SimulatedMob other) {
	}

	/**
	 * Called on hit, before any armor behaviors are applied
	 *
	 * @param self     The simulated mob this behavior belongs to
	 * @param attacker The simulated mob that attacked this, if null the damage came from an ambient source (effect, enchantment, etc.)
	 * @param damage   The damage the attack did
	 * @return The damage that should be applied
	 */
	public double beforeHit(SimulatedMob self, @Nullable SimulatedMob attacker, double damage) {
		return damage;
	}

	/**
	 * Called on hit, after damage has been dealt
	 *
	 * @param self     The simulated mob this behavior belongs to
	 * @param attacker The simulated mob that attacked this, if null the damage came from an ambient source (effect, enchantment, etc.)
	 */
	public void hit(SimulatedMob self, @Nullable SimulatedMob attacker) {
	}

	/**
	 * Called after this mob's equipment behaviors and base attributes are processed, during {@link SimulatedMob#init()}
	 *
	 * @param self The simulated mob this behavior belongs to
	 */
	public void init(SimulatedMob self) {
	}

	/**
	 * Called to check if this mob is dead
	 *
	 * @param self The simulated mob this behavior belongs to
	 * @return Should this mob die?
	 */
	public SimulatedMob.DeathResult shouldDie(SimulatedMob self) {
		return SimulatedMob.DeathResult.PASS;
	}
	
	/***
	 * Called whenever this mob kills another mob
	 * 
	 * @param self The simulated mob this behavior belongs to
	 * @param other The simulated mob that is about to die
	 */
	public void kill(SimulatedMob self, SimulatedMob other) {
		
	}

	/***
	 * Called whenever this mob kills another mob, after drops have been rolled
	 *
	 * @param self The simulated mob this behavior belongs to
	 * @param other The simulated mob that is about to die
	 * @param stacks The generated loot so far   
	 */
	public void modifyDrops(SimulatedMob self, SimulatedMob other, List<ItemStack> stacks) {
		
	}
}
