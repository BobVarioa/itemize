package com.bobvarioa.mobitems.blocks;

import com.bobvarioa.mobitems.blocks.entities.SoulInserterEntity;
import com.bobvarioa.mobitems.register.ModAttachments;
import com.bobvarioa.mobitems.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SoulInserter extends Block implements EntityBlock {
    public SoulInserter(BlockBehaviour.Properties props) {
        super(props);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.SOUL_INSERTER_ENTITY.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> beType) {
        return beType == ModBlockEntities.SOUL_INSERTER_ENTITY.get() ? SoulInserterEntity::tick : null;
    }

    @Override
    public void onRemove(BlockState pState, Level level, BlockPos pos, BlockState pNewState, boolean pIsMoving) {
        if (level.isClientSide) return;
        if (!pNewState.isAir()) return;
        if (level.getBlockEntity(pos) instanceof SoulInserterEntity be) {
            NonNullList<ItemStack> list = NonNullList.create();
            list.add(be.getData(ModAttachments.SOUL_INSERTER.get()).getStackInSlot(0));
            Containers.dropContents(level, pos, list);
        }

    }
}
