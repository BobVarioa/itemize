package com.bobvarioa.mobitems.items;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.blocks.entities.MobBlockEntity;
import com.bobvarioa.mobitems.helpers.EntityUtils;
import com.bobvarioa.mobitems.register.ModBlocks;
import com.bobvarioa.mobitems.register.ModDataComponents;
import com.bobvarioa.mobitems.register.ModItems;
import com.bobvarioa.mobitems.render.MobItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class MobItem extends Item {

    public MobItem(Properties pProperties) {
        super(pProperties);
    }

    public static CompoundTag getEntityData(ItemStack item) {
        var components = item.getComponents();
        if (components.isEmpty()) {
            return new CompoundTag();
        }

        var entityData = components.get(DataComponents.ENTITY_DATA);
        if (entityData == null) {
            return new CompoundTag();
        }

        return entityData.copyTag();
    }


    public static ItemStack mobItemOf(Level level, EntityType<?> type) {
        var entity = type.create(level);
        if (entity instanceof LivingEntity e) {
            return EntityUtils.entityToItem(e, true);
        }

        var item = new ItemStack(ModItems.MOB_ITEM.get());
        var tag = new CompoundTag();
        tag.putString("id", EntityType.getKey(type).toString());
        item.set(DataComponents.ENTITY_DATA, CustomData.of(tag));
        return item;
    }

    @Override
    public Component getName(ItemStack item) {
        var tag = getEntityData(item);

        var name = tag.getString("CustomName");
        if (!name.isEmpty()) {
            return Component.Serializer.fromJsonLenient(name, Minecraft.getInstance().level.registryAccess());
        }

        var str = tag.getString("id");
        if (str.isEmpty()) {
            return Component.literal("Empty Mob");
        }

        var id = ResourceLocation.parse(str);
        return Component.translatable("entity." + id.toLanguageKey());
    }

	@Override
	public boolean isFoil(ItemStack stack) {
		return stack.has(ModDataComponents.MOB_ENCHANTMENTS.get());
	}

	private final NumberFormat formatter = new DecimalFormat("#.##");

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        var nbt = getEntityData(stack);
		
        if (!nbt.isEmpty()) {
			float health = nbt.getFloat("Health");
			float soul = nbt.getFloat(EntityUtils.SOUL_KEY);
			float usedSoul = nbt.getFloat(EntityUtils.USED_SOUL_KEY);

            tooltipComponents.add(Component.literal("Health: " + formatter.format(health)));
			if (soul != 0.0f) {
				tooltipComponents.add(Component.literal("Soul: " + formatter.format(soul)));
			}
			if (usedSoul != 0.0f) {
				tooltipComponents.add(Component.literal("Used soul: " + formatter.format(usedSoul)));
			}
			
			var enchants = stack.get(ModDataComponents.MOB_ENCHANTMENTS.get());
			if (enchants == null) return;
			
			tooltipComponents.add(Component.empty());
			for (var enchant : enchants.toList()) {
				tooltipComponents.add(Component.translatable(enchant.id().toLanguageKey("tooltip.mob_enchantment")));
			}
        }
    }

    public EntityType<?> getType(@Nullable CompoundTag entityTag) {
        if (entityTag != null && entityTag.contains("id", 8)) {
            return EntityType.byString(entityTag.getString("id")).orElse(EntityType.PIG);
        }

        return EntityType.PIG;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack item = pContext.getItemInHand();
            BlockPos blockpos = pContext.getClickedPos();
            Direction direction = pContext.getClickedFace();
            BlockState blockstate = level.getBlockState(blockpos);


            Player player = pContext.getPlayer();

            if (player != null && player.isShiftKeyDown()) {
                var blockState = ModBlocks.MOB_BLOCK.get().defaultBlockState();
                var pos = blockpos.relative(direction);
                if (level.setBlock(pos, blockState, 2)) {
                    if (level.getBlockEntity(pos) instanceof MobBlockEntity be) {
                        be.entityTag = getEntityData(item);
                        var playerPos = player.blockPosition();
                        var bePos = blockpos;
                        if (bePos.getX() > playerPos.getX()) {
                            if (bePos.getZ() > playerPos.getZ()) {
                                be.rotationDegrees = -135.0f;
                            } else if (bePos.getZ() < playerPos.getZ()) {
                                be.rotationDegrees = -45.0f;
                            } else {
                                be.rotationDegrees = -90.0f;
                            }
                        } else if (bePos.getX() < playerPos.getX()) {
                            if (bePos.getZ() > playerPos.getZ()) {
                                be.rotationDegrees = 135.0f;
                            } else if (bePos.getZ() < playerPos.getZ()) {
                                be.rotationDegrees = 45.0f;
                            } else {
                                be.rotationDegrees = 90.0f;
                            }
                        } else {
                            if (bePos.getZ() > playerPos.getZ()) {
                                be.rotationDegrees = -180.0f;
                            } else if (bePos.getZ() < playerPos.getZ()) {
                                be.rotationDegrees = 0.0f;
                            } else {
                                be.rotationDegrees = 90.0f;
                            }
                        }

                        item.shrink(1);
                        level.gameEvent(pContext.getPlayer(), GameEvent.BLOCK_PLACE, blockpos);
                    }
                    return InteractionResult.SUCCESS;
                }
            } else {
                BlockPos blockposCollision;
                if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
                    blockposCollision = blockpos;
                } else {
                    blockposCollision = blockpos.relative(direction);
                }
                CompoundTag tag = getEntityData(item);
                if (!tag.isEmpty()) {
                    EntityType<?> entitytype = getType(tag);
                    var entity = EntityUtils.spawnWithRotation(
                            (ServerLevel) level,
                            entitytype,
                            item,
                            blockposCollision,
                            player != null ? player.position() : null,
                            !Objects.equals(blockpos, blockposCollision) && direction == Direction.UP
                    );
                    if (entity != null) {
                        item.shrink(1);
                        level.gameEvent(pContext.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);
                    }
                }
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return MobItemRenderer.getInstance();
            }
        });
    }
	
	
}
