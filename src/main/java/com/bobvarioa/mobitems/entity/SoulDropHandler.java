package com.bobvarioa.mobitems.entity;

import com.bobvarioa.mobitems.helpers.EntityUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.ArrayList;
import java.util.Collection;

public class SoulDropHandler {
	public static void entityDrops(LivingDropsEvent event) {
		LivingEntity entity = event.getEntity();
		ServerLevel level = null;
		if (entity.level() instanceof ServerLevel sl) {
			level = sl;
		} else return;
		
		RandomSource random = level.getRandom();
		DropAccessor dropAccessor = (DropAccessor) entity;
		DamageSource damageSource = event.getSource();
		
		float soul = EntityUtils.getUsedSoul(entity);
		
		int chances = (int) Math.floor(soul / getDefaultMaxHealth(entity));
		if (chances >= 1) {
			entity.captureDrops(new ArrayList<>());
			boolean flag = event.isRecentlyHit();
			while (chances >= 1) {
				if (random.nextIntBetweenInclusive(1, 10) <= 6) {
					dropAccessor.mobitems$dropFromLootTable(damageSource, flag);
					dropAccessor.mobitems$dropCustomDeathLoot(level, damageSource, flag);
					dropAccessor.mobitems$dropEquipment();
					dropAccessor.mobitems$dropExperience(damageSource.getEntity());
				}
				chances--;
			}
			Collection<ItemEntity> additionalDrops = entity.captureDrops(null);
			Collection<ItemEntity> drops = event.getDrops();
			drops.addAll(additionalDrops);
			
			for (var d1 : drops) {
				for (var d2 : drops) {
					if (d1 == d2 || d1.isRemoved() || d2.isRemoved()) continue; 
					ItemStack itemstack1 = d1.getItem();
					ItemStack itemstack2 = d2.getItem();
					if (ItemEntity.areMergable(itemstack1, itemstack2)) {
						ItemStack merged = ItemEntity.merge(itemstack1, itemstack2, 64);
						d1.setItem(merged);
						if (itemstack2.isEmpty()) {
							d2.discard();
						}
					}

				}
			}
		}
	}

	private static float getDefaultMaxHealth(LivingEntity entity) {
		var attrMap =  new AttributeMap(DefaultAttributes.getSupplier((EntityType<? extends LivingEntity>) entity.getType()));
		return (float) attrMap.getValue(Attributes.MAX_HEALTH);
	}
}
