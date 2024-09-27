package com.bobvarioa.mobitems.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public interface DropAccessor {
	void mobitems$dropFromLootTable(DamageSource source, boolean flag);

	void mobitems$dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean flag);

	void mobitems$dropEquipment();

	void mobitems$dropExperience(Entity entity);
	
}
