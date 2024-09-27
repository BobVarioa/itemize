package com.bobvarioa.mobitems.helpers;

import com.bobvarioa.mobitems.items.MobItem;
import com.bobvarioa.mobitems.register.ModAttachments;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

import static com.bobvarioa.mobitems.register.ModItems.MOB_ITEM;
import static com.bobvarioa.mobitems.MobItems.MODID;

public class EntityUtils {

    public static final String SOUL_KEY = MODID + "Soul";
    public static final String USED_SOUL_KEY = MODID + "PrevSoul";
    public static ItemStack entityToItem(LivingEntity entity) {
        return entityToItem(entity, false);
    }

    public static ItemStack entityToItem(LivingEntity entity, boolean makeCanonical) {
        var item = new ItemStack(MOB_ITEM.get());
        var tag = new CompoundTag();
        String id = entity.getEncodeId();
        if (id != null) {
            tag.putString("id", id);
        }
		if (entity instanceof Mob mob) {
			mob.dropLeash(true, true);
		}
		
		var soul = getSoul(entity);
		var usedSoul = getUsedSoul(entity);
		tag.putFloat(EntityUtils.SOUL_KEY, soul);
		tag.putFloat(EntityUtils.USED_SOUL_KEY, usedSoul);
		setSoul(entity, 0);
		setUsedSoul(entity, 0);
		
		// remove attribute minecraft:random_spawn_bonus
		
        entity.saveWithoutId(tag);

		// all living entities
        tag.remove("Pos");
        tag.remove("UUID");
        tag.remove("Motion");
        tag.remove("Rotation");
        tag.remove("Air");
        tag.remove("Fire");
        tag.remove("PortalCooldown");
		tag.remove("TicksFrozen");
        tag.putByte("OnGround", (byte) 1);
		// mobs
        tag.remove("HurtByTimestamp");
        tag.remove("HurtTime");
        tag.remove("DeathTime");
		tag.remove("SleepingX");
		tag.remove("SleepingY");
		tag.remove("SleepingZ");
		// mobs that can breed
		tag.remove("InLove");
		tag.remove("LoveCause");
		
		
		ResourceLocation key = EntityType.getKey(entity.getType());
		if (key.getNamespace().equals("minecraft")) switch (key.getPath()) {
			case "armadillo" -> tag.remove("scute_time");
			case "axolotl", "cod", "pufferfish", "salmon", "tadpole", "tropical_fish" -> tag.remove("FromBucket");
			case "chicken" -> tag.remove("EggLayTime");
			case "bat" -> tag.remove("BatFlags");
			case "bee" -> {
				tag.remove("CannotEnterHiveTicks");
				tag.remove("CropsGrownSincePollination");
				tag.remove("flower_pos");
				tag.remove("hive_pos");
				tag.remove("TicksSincePollination");
			}
			case "creeper" -> {
				tag.remove("Fuse");
				tag.remove("ignited");
			}
			case "zombie", "husk", "drowned" -> {
				tag.remove("InWaterTime");
				tag.remove("DrownedConversionTime");
			}
			case "ender_dragon" -> tag.remove("DragonPhase");
			case "endermite" -> tag.putInt("Lifetime", 0);
			case "fox" -> {
				tag.remove("Crouching");
				tag.remove("Sitting");
				tag.remove("Sleeping");
			}
			case "glow_squid" -> tag.remove("DarkTicksRemaining");
			case "hoglin", "piglin", "piglin_brute", "zombified_piglin" -> {
				tag.remove("TimeInOverworld");
				tag.remove("AngerTime");
				tag.remove("AngerAt");
				// idk why piglins have this?? they can't convert into drowned, right?
				tag.remove("InWaterTime");
				tag.remove("DrownedConversionTime");
			}
			case "magma_cube", "slime" -> tag.putBoolean("wasOnGround", true);
			case "phantom" -> {
				tag.remove("AX");
				tag.remove("AY");
				tag.remove("AZ");
			}
			case "rabbit" -> tag.remove("MoreCarrotTicks");
			case "pillager", "ravager", "evoker", "illusioner", "vindicator", "witch" -> {
				tag.remove("CanJoinRaid");
				tag.remove("Patrolling");
				tag.remove("patrol_target");
				tag.remove("RaidId");
				tag.remove("Wave");
				// ravager
				tag.remove("AttackTick");
				tag.remove("RoarTick");
				tag.remove("StunTick");
				// evoker, illusioner
				tag.remove("SpellTicks");
			}
			case "shulker" -> {
				tag.remove("APX");
				tag.remove("APY");
				tag.remove("APZ");
				tag.remove("AttachFace");
				tag.putByte("Peek", (byte)4);
			}
			case "skeleton" -> tag.putInt("StrayConversionTime", -1);
			case "horse", "skeleton_horse", "zombie_horse", "trader_llama", "llama" -> {
				tag.remove("EatingHaystack");
				// skeleton horse
				tag.remove("SkeletonTrap");
				tag.remove("SkeletonTrapTime");
			}
			case "turtle" -> {
				tag.remove("TravelPosX");
				tag.remove("TravelPosY");
				tag.remove("TravelPosZ");
			}
			case "vex" -> {
				tag.remove("BoundX");
				tag.remove("BoundY");
				tag.remove("BoundZ");
				tag.putInt("LifeTicks", 20);
			}
			case "wandering_trader" -> {
				tag.remove("wander_target");
				tag.remove("DespawnDelay"); // todo: default value?
			}
			case "warden" -> tag.remove("anger");
			case "zombie_villager" -> {
				tag.remove("ConversionTime");
				tag.remove("ConversionPlayer");
			}
			
        }
        
		
        if (makeCanonical) {
			// all mobs
            tag.remove("LeftHanded");
            tag.remove("Brain");
			tag.remove("attributes");
			tag.remove("ArmorDropChances");
			tag.remove("ArmorItems");
			tag.remove("body_armor_drop_chance");
			tag.remove("body_armor_item");
			tag.remove("CanPickUpLoot");
			tag.remove("HandDropChances");
			tag.remove("HandItems");
			// there's probably some stuff abt villagers i missed
        }

		// custom
        if (tag.getTagType("MaxHealth") == 0) {
            tag.putFloat("MaxHealth", entity.getMaxHealth());
        }

        item.set(DataComponents.ENTITY_DATA, CustomData.of(tag));
        return item;
    }


	public static Entity spawnWithRotation(ServerLevel level, EntityType<?> entitytype, ItemStack stack, BlockPos pos, @Nullable Vec3 lookAtPos, boolean pShouldOffsetYMore) {
		return spawnWithRotation(level, entitytype, MobItem.getEntityData(stack), pos, lookAtPos, pShouldOffsetYMore);
	}

    public static Entity spawnWithRotation(ServerLevel level, EntityType<?> entitytype, CompoundTag tag, BlockPos pos, @Nullable Vec3 lookAtPos, boolean pShouldOffsetYMore) {
        Entity entity = entitytype.create(
                level,
                null,
                pos,
                MobSpawnType.SPAWN_EGG,
                true,
                pShouldOffsetYMore
        );
        if (entity != null) {
			if (entity instanceof LivingEntity le) {
				setSoul(le, getSoul(le) + tag.getFloat(SOUL_KEY));
				setUsedSoul(le, getUsedSoul(le) + tag.getFloat(USED_SOUL_KEY));
				tag.remove(SOUL_KEY);
				tag.remove(USED_SOUL_KEY);
			}
			CustomData.of(tag).loadInto(entity);

            if (lookAtPos != null) {
                entity.lookAt(EntityAnchorArgument.Anchor.FEET, lookAtPos);
            }

            level.addFreshEntityWithPassengers(entity);

			if (entity instanceof LivingEntity le) {
				EntityUtils.applySoulEffects(le);
			}
        }
        return entity;
    }

    public static void addAttribute(AttributeMap attrs, Holder<Attribute> attr, AttributeModifier.Operation op, double val) {
        var inst = attrs.getInstance(attr);
        if (inst == null) return;
        inst.addPermanentModifier(new AttributeModifier(attr.getKey().location(), val, op));

    }

    public static void applySoulEffects(LivingEntity entity) {
        float soul = EntityUtils.getSoul(entity);
        if (soul <= 0.0f) return;
        var attrs = entity.getAttributes();
        addAttribute(attrs, Attributes.MAX_HEALTH, AttributeModifier.Operation.ADD_VALUE, Math.floor(soul));
        entity.heal((float) Math.floor(soul));
        addAttribute(attrs, Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADD_VALUE, Math.floor(soul / 25));
        addAttribute(attrs, Attributes.MOVEMENT_SPEED, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 1 + (Math.floor(soul / 50) / 2));
        setSoul(entity, 0.0f);
        setUsedSoul(entity, getUsedSoul(entity) + soul);
    }

    public static float getSoul(LivingEntity entity) {
        return entity.getData(ModAttachments.SOUL.get());
    }


    public static void setSoul(LivingEntity entity, float soul) {
		entity.setData(ModAttachments.SOUL.get(), soul);
    }


    public static float getUsedSoul(LivingEntity entity) {
        return entity.getData(ModAttachments.USED_SOUL.get());
    }


    public static void setUsedSoul(LivingEntity entity, float usedSoul) {
		entity.setData(ModAttachments.USED_SOUL.get(), usedSoul);
    }
}
