package com.bobvarioa.mobitems.items;

import com.bobvarioa.mobitems.entity.EntityUtils;
import com.bobvarioa.mobitems.register.ModSounds;
import com.bobvarioa.mobitems.sounds.ExtendedSoundManager;
import com.bobvarioa.mobitems.register.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ConverterItem extends Item {
    public enum Types {
        CREATIVE,
        NORMAL,
        VACUUM
    }

    public Types type;

    public ConverterItem(Properties pProperties, Types type) {
        super(pProperties);
        this.type = type;
    }

    public int ticks = 0;
    public long lastUsed = 0;

    public static int TICK_SECOND = 20;

    public int clientTicks = 0;
    public long lastVacuumTime = System.currentTimeMillis();

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand usedHand) {
        boolean canPickup = switch (type) {
            case CREATIVE -> true;
            case NORMAL -> entity.getHealth() <= 20;
            case VACUUM -> entity.getHealth() <= 5;
        };

        if (canPickup) {
            var level = player.level();
            var worldPos = entity.getOnPos();

            if (level.isClientSide) {
                if (type == Types.VACUUM) {
                    if (clientTicks < TICK_SECOND * 3) {
                        clientTicks++;
                        if (System.currentTimeMillis() - lastVacuumTime > 1050) {
                            player.playSound(ModSounds.VACUUM_LOOP.get());
                            lastVacuumTime = System.currentTimeMillis();
                        }
                        return InteractionResult.PASS;
                    } else {
                        clientTicks = 0;
                    }
                }

                return InteractionResult.PASS;
            }

            if (type != Types.CREATIVE && ticks < TICK_SECOND * 3) {
                ticks++;
                lastUsed = level.getGameTime();

                if (type == Types.VACUUM) {
                    double speedFactor = 0.5;
                    if (entity.position().closerThan(player.position(), 1)) {
                        speedFactor = 0.01;
                    }
                    entity.addDeltaMovement(player.getLookAngle().normalize().multiply(-1, -1, -1).multiply(speedFactor, speedFactor, speedFactor));
                }

                return InteractionResult.PASS;
            }

            if (type == Types.CREATIVE || type == Types.NORMAL) {
                var entityItem = EntityUtils.entityToItem(entity);
                Containers.dropItemStack(level, worldPos.getX(), worldPos.getY() - 1, worldPos.getZ(), entityItem);
            } else {
                Containers.dropItemStack(level, worldPos.getX(), worldPos.getY() - 1, worldPos.getZ(), new ItemStack(ModItems.DUSTY_MOB_SOUL.get()));
            }
            level.playSound(entity, entity.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1f, 1f);
            entity.remove(Entity.RemovalReason.DISCARDED);
            ticks = 0;

            if (type != Types.CREATIVE && !player.isCreative()) {
                player.getCooldowns().addCooldown(stack.getItem(), TICK_SECOND * 60);
            }

            return InteractionResult.CONSUME;
        }

        return super.interactLivingEntity(stack, player, entity, usedHand);
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return ticks != 0;
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return Mth.hsvToRgb(179f / 255f, 1.0f, 1.0f);
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        float max = TICK_SECOND * 3;
        float current = ticks;
        return Math.round((current/max) * 13);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        long diff = level.getGameTime() - lastUsed;
        int gracePeriod = TICK_SECOND * 6;
        if (diff > gracePeriod && ticks > 0) {
            ticks--;
        }
        if (type == Types.VACUUM && level.isClientSide) {
            if (diff > gracePeriod && clientTicks > 0) {
                clientTicks--;
                if (System.currentTimeMillis() - lastVacuumTime > 100) {
                    SoundManager soundManager = Minecraft.getInstance().getSoundManager();
                    ResourceLocation loop = ModSounds.VACUUM_LOOP.get().getLocation();
                    if (((ExtendedSoundManager)soundManager).mobitems$isActive(loop, SoundSource.PLAYERS)) {
                        soundManager.stop(loop, SoundSource.PLAYERS);
                    }
                }
            }
        }
    }
}
