package com.bobvarioa.mobitems.blocks;

import com.bobvarioa.mobitems.blocks.entities.MobBlockEntity;
import com.bobvarioa.mobitems.helpers.EntityUtils;
import com.bobvarioa.mobitems.items.ConverterItem;
import com.bobvarioa.mobitems.register.ModBlockEntities;
import com.bobvarioa.mobitems.register.ModBlocks;
import com.bobvarioa.mobitems.register.ModItems;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class MobBlock extends Block implements EntityBlock {
    public MobBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.MOB_BLOCK_ENTITY.get().create(pos, state);
    }
    @Override

    protected void spawnDestroyParticles(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState) {
        return;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (level.isClientSide) return;
        if (!newState.isAir()) return;
        var be = ModBlockEntities.MOB_BLOCK_ENTITY.get().getBlockEntity(level, pos);
        if (be != null && !be.destroyHandled) {
            if (be.entityTag.getFloat("Health") <= 0.0f) return;
            EntityType<?> entitytype = EntityType.byString(be.entityTag.getString("id")).orElse(EntityType.PIG);
			var entity = EntityUtils.spawnWithRotation((ServerLevel) level, entitytype, be.entityTag, pos, null, false);
            if (entity != null) {
                level.gameEvent(entity, GameEvent.ENTITY_PLACE, pos);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        var be = ModBlockEntities.MOB_BLOCK_ENTITY.get().getBlockEntity(level, pos);
        if (be != null) {
            if (player.isShiftKeyDown()) {
                be.rotationDegrees += 15;
            } else {
                be.rotationDegrees += 45;
            }
            be.rotationDegrees = Mth.wrapDegrees(be.rotationDegrees);
            return InteractionResult.CONSUME;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.getItem() instanceof ConverterItem item) {
			if (item.type == ConverterItem.Types.VACUUM) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
			
			if (state.is(ModBlocks.MOB_BLOCK) && level.getBlockEntity(pos) instanceof MobBlockEntity be) {
				var droppedStack = new ItemStack(ModItems.MOB_ITEM.get());
				droppedStack.set(DataComponents.ENTITY_DATA, CustomData.of(be.entityTag));
				be.destroyHandled = true;
				player.addItem(droppedStack);
				if (droppedStack.getCount() == 0) {
					level.removeBlock(pos, false);
				} else {
					Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), droppedStack);
				}
			}
			
		}
		
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}

	@Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        var be = ModBlockEntities.MOB_BLOCK_ENTITY.get().getBlockEntity(level, pos);
        if (be != null) {
            var stack = new ItemStack(ModItems.MOB_ITEM.get());
            stack.set(DataComponents.ENTITY_DATA, CustomData.of(be.entityTag));
            return stack;
        }
        return null;
    }

    @Override
    public void initializeClient(Consumer<IClientBlockExtensions> consumer) {
        consumer.accept(new IClientBlockExtensions() {
            @Override
            public boolean addDestroyEffects(BlockState state, Level Level, BlockPos pos, ParticleEngine manager) {
                return true; // true means we successfully created particles, so no other particles should be created, and since we didn't create anything, particles are disabled
            }
        });
    }
}
