package com.bobvarioa.mobitems.entity;

import com.bobvarioa.mobitems.items.MobItem;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.phys.Vec3;

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
        String id = entity.self().getEncodeId();
        if (id != null) {
            tag.putString("id", entity.self().getEncodeId());
        }
        entity.self().saveWithoutId(tag);

        tag.remove("Pos");
        tag.remove("UUID");
        tag.remove("Motion");
        tag.remove("Rotation");
        tag.remove("HurtByTimestamp");
        tag.remove("HurtTime");
        tag.remove("Air");
        tag.remove("DeathTime");
        tag.remove("Fire");
        tag.remove("EggLayTime");
        tag.remove("PortalCooldown");
        tag.putByte("OnGround", (byte) 1);

        if (tag.getTagType("MaxHealth") == 0) {
            tag.putFloat("MaxHealth", entity.getMaxHealth());
        }

        if (makeCanonical) {
            tag.remove("LeftHanded");
            tag.remove("Brain");
            // todo: there's likely a lot of these
        }

        item.set(DataComponents.ENTITY_DATA, CustomData.of(tag));
        return item;
    }

    public static Entity spawnWithRotation(ServerLevel level, EntityType<?> entitytype, ItemStack stack, BlockPos pos, Vec3 lookAtPos, boolean pShouldOffsetYMore) {
        Entity entity = entitytype.create(
                level,
                null,
                pos,
                MobSpawnType.SPAWN_EGG,
                true,
                pShouldOffsetYMore
        );
        if (entity != null) {
            var tag = MobItem.getEntityData(stack);
            CustomData.of(tag).loadInto(entity);

            if (lookAtPos != null) {
                entity.lookAt(EntityAnchorArgument.Anchor.FEET, lookAtPos);
            }

            level.addFreshEntityWithPassengers(entity);
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
        return ((EntitySoulAccessor) entity).getSoul();
    }


    public static void setSoul(LivingEntity entity, float soul) {
        ((EntitySoulAccessor) entity).setSoul(soul);
    }


    public static float getUsedSoul(LivingEntity entity) {
        return ((EntitySoulAccessor) entity).getUsedSoul();
    }


    public static void setUsedSoul(LivingEntity entity, float usedSoul) {
        ((EntitySoulAccessor) entity).setUsedSoul(usedSoul);
    }

//    public static void onEntityDeath(LivingDropsEvent event) {
//        var entity = event.getEntity();
//    }
}
