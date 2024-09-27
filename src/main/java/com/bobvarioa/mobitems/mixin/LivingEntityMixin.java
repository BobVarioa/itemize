package com.bobvarioa.mobitems.mixin;

import com.bobvarioa.mobitems.entity.DropAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements DropAccessor {
	
	@Invoker("dropFromLootTable")
	public abstract void mobitems$dropFromLootTable(DamageSource source, boolean flag);
	@Invoker("dropCustomDeathLoot")
	public abstract void mobitems$dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean flag);
	@Invoker("dropEquipment")
	public abstract void mobitems$dropEquipment();
	@Invoker("dropExperience")
	public abstract void mobitems$dropExperience(Entity entity);


}