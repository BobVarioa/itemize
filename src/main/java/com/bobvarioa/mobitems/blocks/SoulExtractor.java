package com.bobvarioa.mobitems.blocks;

import com.bobvarioa.mobitems.blocks.entities.SoulExtractorEntity;
import com.bobvarioa.mobitems.register.ModAttachments;
import com.bobvarioa.mobitems.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class SoulExtractor extends Block implements EntityBlock {
    public SoulExtractor(Properties props) {
        super(props);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.SOUL_EXTRACTOR_ENTITY.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> beType) {
        return beType == ModBlockEntities.SOUL_EXTRACTOR_ENTITY.get() ? SoulExtractorEntity::tick : null;
    }

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide) return InteractionResult.CONSUME;
		if (level.getBlockEntity(pos) instanceof SoulExtractorEntity be) {
			var itemHandler = be.getData(ModAttachments.SOUL_EXTRACTOR.get());
			if (player.getItemInHand(player.getUsedItemHand()).isEmpty()) {
				player.setItemInHand(player.getUsedItemHand(), itemHandler.extractItem(0, 64, false));
			}
			return InteractionResult.CONSUME;
		}
		return InteractionResult.PASS;
	}

	@Override
	public void onRemove(BlockState pState, Level level, BlockPos pos, BlockState pNewState, boolean pIsMoving) {
		if (level.isClientSide) return;
		if (!pNewState.isAir()) return;
		if (level.getBlockEntity(pos) instanceof SoulExtractorEntity be) {
			NonNullList<ItemStack> list = NonNullList.create();
			list.add(be.getData(ModAttachments.SOUL_EXTRACTOR.get()).getStackInSlot(0));
			Containers.dropContents(level, pos, list);
		}

	}
}
