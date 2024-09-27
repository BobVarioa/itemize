package com.bobvarioa.mobitems.blocks;

import com.bobvarioa.mobitems.blocks.entities.ConverterEntity;
import com.bobvarioa.mobitems.register.ModBlockEntities;
import com.bobvarioa.mobitems.register.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class Converter extends Block implements EntityBlock {
    public Converter(Properties props) {
        super(props);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.CONVERTER_ENTITY.get().create(pos, state);
    }

    @Override
    protected void spawnDestroyParticles(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState) {
        super.spawnDestroyParticles(pLevel, pPlayer, pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> beType) {
        return beType == ModBlockEntities.CONVERTER_ENTITY.get() ? ConverterEntity::tick : null;
    }


    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (pLevel.getBlockEntity(pPos) instanceof ConverterEntity blockEntity) {
            var pos = pPos.below();
            var block = pLevel.getBlockState(pos);
            while (block.is(ModBlocks.CONVERTER_CASING.get())) {
                pLevel.setBlock(pos, block.setValue(ConverterCasing.EMPTY, false), 2);

                pos = pos.below();
                block = pLevel.getBlockState(pos);
            }
            pos = pos.above();

            blockEntity.updateCasingChain(pos);
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pNewState.isAir() || pNewState.is(ModBlocks.SPAWN_CAPTURER.get())) return;
        var pos = pPos.below();
        var block = pLevel.getBlockState(pos);
        while (block.is(ModBlocks.CONVERTER_CASING.get())) {
            pLevel.setBlock(pos, block.setValue(ConverterCasing.EMPTY, true), 2);

            pos = pos.below();
            block = pLevel.getBlockState(pos);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            ItemStack item = player.getItemInHand(hand);
            if (item.is(Items.NETHER_STAR)) {
                if (!player.isCreative()) item.shrink(1);
                level.setBlock(pos, ModBlocks.SPAWN_CAPTURER.get().defaultBlockState(), 2);
            	return ItemInteractionResult.CONSUME;
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
