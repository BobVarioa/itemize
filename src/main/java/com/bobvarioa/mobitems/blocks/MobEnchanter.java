package com.bobvarioa.mobitems.blocks;

import com.bobvarioa.mobitems.blocks.entities.MobColosseumEntity;
import com.bobvarioa.mobitems.blocks.entities.MobEnchanterEntity;
import com.bobvarioa.mobitems.blocks.entities.MobModifierEntity;
import com.bobvarioa.mobitems.gui.menu.MobColosseumMenu;
import com.bobvarioa.mobitems.gui.menu.MobEnchanterMenu;
import com.bobvarioa.mobitems.gui.menu.MobModifierMenu;
import com.bobvarioa.mobitems.register.ModAttachments;
import com.bobvarioa.mobitems.register.ModBlockEntities;
import com.bobvarioa.mobitems.register.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MobEnchanter extends Block implements SimpleWaterloggedBlock, EntityBlock {
	public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.NORTH, Direction.EAST, Direction.WEST, Direction.SOUTH);
	
	public MobEnchanter(Properties properties) {
		super(properties);
		this.registerDefaultState(
			this.getStateDefinition().any()
				.setValue(FACING, Direction.NORTH)
				.setValue(BlockStateProperties.WATERLOGGED, false)
		);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
		builder.add(BlockStateProperties.WATERLOGGED);
	}
	

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		boolean inWater = false;
		if (context.getLevel().getBlockState(context.getClickedPos()).is(Blocks.WATER)) {
			inWater = true;
		}
		var state = defaultBlockState()
			.setValue(FACING, context.getHorizontalDirection());
		if (inWater) {
			state.setValue(BlockStateProperties.WATERLOGGED, true);
		}
		return state;
	}


	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
		return SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluidState);
	}

	@Override
	public boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
		return SimpleWaterloggedBlock.super.canPlaceLiquid(player, level, pos, state, fluid);
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(BlockStateProperties.WATERLOGGED)) {
			level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return Shapes.or(
			Block.box(0, 5, 0, 16, 11, 16),
			Block.box(0, 0, 0, 4, 5, 4),
			Block.box(12, 0, 0, 16, 5, 4),
			Block.box(0, 0, 12, 4, 5, 16),
			Block.box(12, 0, 12, 16, 5, 16)
		);
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
		if (!level.isClientSide) {
			// run enchant on rising signal
			if (level.hasNeighborSignal(pos)) {
				if (level.getBlockEntity(pos) instanceof MobEnchanterEntity be) {
					be.enchant(true);
				}
			}
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return ModBlockEntities.MOB_ENCHANTER.get().create(blockPos, blockState);
	}


	@Nullable
	@Override
	protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof MobEnchanterEntity be) {
			return new SimpleMenuProvider((id, inv, player) -> new MobEnchanterMenu(id, inv, be), Component.translatable("container.mob_enchanter"));
		}
		throw new IllegalStateException("Incorrect block entity class (%s) for MobColosseum!"
			.formatted(blockEntity.getClass().getCanonicalName()));
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if ((stack.is(ModItems.RITUAL_TABLET.get()) || stack.is(ModItems.CREATIVE_POCKET_CONVERTER.get())) && level.getBlockEntity(pos) instanceof MobEnchanterEntity be) {
			be.enchant(false);
			return ItemInteractionResult.CONSUME;
		}
		
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
			serverPlayer.openMenu(state.getMenuProvider(level, pos));
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}


	@Override
	public void onRemove(BlockState pState, Level level, BlockPos pos, BlockState pNewState, boolean pIsMoving) {
		if (level.isClientSide) return;
		if (!pNewState.isAir()) return;
		if (level.getBlockEntity(pos) instanceof MobEnchanterEntity be) {
			NonNullList<ItemStack> list = NonNullList.create();
			var itemHandler = be.getData(ModAttachments.MOB_ENCHANTER.get());
			list.add(itemHandler.getStackInSlot(0));
			list.add(itemHandler.getStackInSlot(1));
			list.add(itemHandler.getStackInSlot(2));
			list.add(itemHandler.getStackInSlot(3));
			Containers.dropContents(level, pos, list);
		}

	}
}
