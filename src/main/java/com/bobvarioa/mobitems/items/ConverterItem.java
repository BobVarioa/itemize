package com.bobvarioa.mobitems.items;

import com.bobvarioa.mobitems.helpers.EntityUtils;
import com.bobvarioa.mobitems.register.ModSounds;
import com.bobvarioa.mobitems.sounds.ExtendedSoundManager;
import com.bobvarioa.mobitems.register.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

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
	public static int VACUUM_DURATION = TICK_SECOND * 3;
	

	public int clientTicks = 0;
	public long lastVacuumTime = System.currentTimeMillis();

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
		return Math.round(((float)ticks / VACUUM_DURATION) * 13);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		long diff = level.getGameTime() - lastUsed;
		int gracePeriod = TICK_SECOND * 6;
		if (diff > gracePeriod && ticks > 0) {
			ticks--;
		}
		if (diff > gracePeriod && clientTicks > 0) {
			clientTicks--;
		}
	}
	
	private static HitResult getHitResult(Player player) {
		return ProjectileUtil.getHitResultOnViewVector(player, (e) -> !e.isSpectator() && e.isPickable(), player.entityInteractionRange());
	}


	private void convertEntityToItem(Level level, Player player, LivingEntity entity, ItemStack stack) {
		var worldPos = entity.getOnPos();

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
			player.getCooldowns().addCooldown(stack.getItem(), type == Types.VACUUM ? TICK_SECOND * 30 : TICK_SECOND * 60);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		ItemStack stack = player.getItemInHand(usedHand);
		LivingEntity entity = null;
		HitResult hitResult = Minecraft.getInstance().hitResult;
		if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
			EntityHitResult result = (EntityHitResult) hitResult;
			if (result.getEntity() instanceof LivingEntity le && !(le instanceof Player)) {
				entity = le;
			}
		}

		if (entity == null) {
			return InteractionResultHolder.pass(stack);
		}

		boolean canPickup = switch (type) {
			case CREATIVE -> true;
			case NORMAL -> entity.getHealth() <= 20;
			case VACUUM -> entity.getHealth() <= 5;
		};

		if (canPickup) {
			if (level.isClientSide ) {
				player.startUsingItem(usedHand);
				if (type == Types.VACUUM) {
					player.playSound(ModSounds.VACUUM_START.get());
					lastVacuumTime = System.currentTimeMillis();
				}
				return InteractionResultHolder.consume(stack);
			}

			if (type != Types.CREATIVE) {
				player.startUsingItem(usedHand);
				return InteractionResultHolder.consume(stack);
			}

			convertEntityToItem(level, player, entity, stack);
			return InteractionResultHolder.consume(stack);
		}

		return super.use(level, player, usedHand);
	}

	@Override
	public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
		super.onUseTick(level, livingEntity, stack, remainingUseDuration);
		Player player = livingEntity instanceof Player p ? p : null;
		if (player == null) return;

		LivingEntity entity = null;
		HitResult hitResult = level.isClientSide ? Minecraft.getInstance().hitResult : getHitResult(player);
		if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
			EntityHitResult result = (EntityHitResult) hitResult;
			if (result.getEntity() instanceof LivingEntity le) {
				entity = le;
			}
		}

		if (level.isClientSide) {
			if (type == Types.VACUUM) {
				if (clientTicks < VACUUM_DURATION) {
					if (System.currentTimeMillis() - lastVacuumTime > 1050) {
						player.playSound(ModSounds.VACUUM_LOOP.get());
						lastVacuumTime = System.currentTimeMillis();
					}
					if (entity != null) clientTicks++;
					return;
				} else {
					clientTicks = 0;
					player.stopUsingItem();
				}
			}

			return;
		}
		
		if (entity == null) return;

		boolean canPickup = switch (type) {
			case CREATIVE -> true;
			case NORMAL -> entity.getHealth() <= 20;
			case VACUUM -> entity.getHealth() <= 5;
		};
		
		if (!canPickup) return;


		if (type != Types.CREATIVE && ticks < VACUUM_DURATION) {
			ticks++;
			lastUsed = level.getGameTime();

			if (type == Types.VACUUM) {
				double speedFactor = 0.5;
				if (entity.position().closerThan(player.position(), 1)) {
					speedFactor = 0.01;
				}
				entity.addDeltaMovement(player.getLookAngle().normalize().multiply(-1, -1, -1).multiply(speedFactor, speedFactor, speedFactor));
			}

			return;
		}

		convertEntityToItem(level, player, entity, stack);
		this.releaseUsing(stack, level, player, 0);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
		if (type == Types.VACUUM && level.isClientSide) {
			SoundManager soundManager = Minecraft.getInstance().getSoundManager();
			ResourceLocation loop = ModSounds.VACUUM_LOOP.get().getLocation();
			if (((ExtendedSoundManager) soundManager).mobitems$isActive(loop, SoundSource.PLAYERS)) {
				soundManager.stop(loop, SoundSource.PLAYERS);
				livingEntity.playSound(ModSounds.VACUUM_END.get());
			}
		}
		super.releaseUsing(stack, level, livingEntity, timeCharged);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
		// todo: eventually the ritual tablets should have a different animation
		// return type == Types.VACUUM ? UseAnim.BOW : UseAnim.CUSTOM;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 24000 * 6;
	}
}
