package com.bobvarioa.mobitems.entity.simulator;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

public enum MobStat {
	// generic.max_health
	MAX_HEALTH(1),
	// generic.attack_damage
	DAMAGE(0),
	// generic.armor
	ARMOR(0),
	// generic.armor_toughness
	ARMOR_TOUGHNESS(0),
	// generic.attack_speed - multiplier to damage (X attacks per turn)
	ATTACK_SPEED(0),
	// generic.attack_knockback - stuns for 1 turn per block knockback
	KNOCKBACK(0),
	// generic.knockback_resistance - resistance percentage to knockback 
	KNOCKBACK_RESISTANCE(0),
	// generic.luck - crit chance 1 - 10
	CRIT_CHANCE(0),
	// the multiplier to damage that crits do, default 2
	CRIT_MULTIPLIER(1),
	// generic.movement_speed - dodge chance
	MOVEMENT_SPEED(0),
	INVALID(-1);

	public final double min;
	
	MobStat(double min) {
		this.min = min;
	} 

	public static MobStat getFor(Holder<Attribute> attr) {
		if (attr.is(Attributes.ARMOR)) {
			return MobStat.ARMOR;
		} else if (attr.is(Attributes.ARMOR_TOUGHNESS)) {
			return MobStat.ARMOR_TOUGHNESS;
		} else if (attr.is(Attributes.ATTACK_DAMAGE)) {
			return MobStat.DAMAGE;
		} else if (attr.is(Attributes.ATTACK_KNOCKBACK)) {
			return MobStat.KNOCKBACK;
		} else if (attr.is(Attributes.KNOCKBACK_RESISTANCE)) {
			return MobStat.KNOCKBACK_RESISTANCE;
		} else if (attr.is(Attributes.ATTACK_SPEED)) {
			return MobStat.ATTACK_SPEED;
		} else if (attr.is(Attributes.LUCK)) {
			return MobStat.CRIT_CHANCE;
		} else if (attr.is(Attributes.MOVEMENT_SPEED)) {
			return MobStat.MOVEMENT_SPEED;
		}

		return MobStat.INVALID;
	}
}
