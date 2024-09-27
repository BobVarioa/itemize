package com.bobvarioa.mobitems.blocks;

import com.bobvarioa.mobitems.blocks.entities.MobColosseumEntity;
import com.bobvarioa.mobitems.gui.menu.MobColosseumMenu;
import com.bobvarioa.mobitems.register.ModAttachments;
import com.bobvarioa.mobitems.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class MobColosseum extends Block implements EntityBlock {
	public static final DirectionProperty FACING = DirectionProperty.create("facing");
	
	public MobColosseum(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return ModBlockEntities.MOB_COLOSSEUM.get().create(pos, state);
	}

	@Nullable
	@Override
	protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof MobColosseumEntity be) {
			return new SimpleMenuProvider((id, inv, player) -> new MobColosseumMenu(id, inv, be), Component.translatable("container.mob_colosseum"));
		}
		throw new IllegalStateException("Incorrect block entity class (%s) for MobColosseum!"
			.formatted(blockEntity.getClass().getCanonicalName()));
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
			serverPlayer.openMenu(state.getMenuProvider(level, pos));
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> beType) {
		return beType == ModBlockEntities.MOB_COLOSSEUM.get() ? MobColosseumEntity::tick : null;
	}
	
	@Override
	public void onRemove(BlockState pState, Level level, BlockPos pos, BlockState pNewState, boolean pIsMoving) {
		if (level.isClientSide) return;
		if (!pNewState.isAir()) return;
		if (level.getBlockEntity(pos) instanceof MobColosseumEntity be) {
			NonNullList<ItemStack> list = NonNullList.create();
			for (int i = 0; i < 16; i++) {
				list.add(be.getData(ModAttachments.MOB_COLOSSEUM.get()).getStackInSlot(i));
			}
			Containers.dropContents(level, pos, list);
		}

	}
}
