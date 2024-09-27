package com.bobvarioa.mobitems.blocks;

import com.bobvarioa.mobitems.blocks.entities.ConverterEntity;
import com.bobvarioa.mobitems.register.ModBlocks;
import com.bobvarioa.mobitems.register.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

public class ConverterCasing extends Block {
    public static final BooleanProperty EMPTY = BooleanProperty.create("empty");


    public ConverterCasing(Properties props) {
        super(props);
		this.registerDefaultState(this.getStateDefinition().any().setValue(EMPTY, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(EMPTY);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }
    public static void fillBlocksBelow(Level pLevel, BlockPos pPos) {
        Block casingBlock = ModBlocks.CONVERTER_CASING.get();
        ConverterEntity blockEntity = null;
        var pos = pPos.above();
        var block = pLevel.getBlockState(pos);
        while (block.is(ModBlocks.CONVERTER.get()) || block.is(casingBlock)) {
            if (block.is(ModBlocks.CONVERTER.get())) {
                if (pLevel.getBlockEntity(pos) instanceof ConverterEntity be) {
                    blockEntity = be;
                }
                break;
            }
            pos = pos.above();
            block = pLevel.getBlockState(pos);
        }

        if (blockEntity != null) {
            pos = pPos;
            block = pLevel.getBlockState(pos);
            while (block.is(casingBlock)) {
                pLevel.setBlock(pos, casingBlock.defaultBlockState().setValue(EMPTY, false), 2);
                pos = pos.below();
                block = pLevel.getBlockState(pos);
            }
            pos = pos.above();

            blockEntity.updateCasingChain(pos);
        }
    }
    public static void emptyBlocksBelow(Level pLevel, BlockPos pPos) {
        Block casingBlock = ModBlocks.CONVERTER_CASING.get();
        var pos = pPos.above();
        var block = pLevel.getBlockState(pos);
        while (block.is(ModBlocks.CONVERTER.get()) || block.is(casingBlock)) {
            if (block.is(ModBlocks.CONVERTER.get())) {
                if (pLevel.getBlockEntity(pos) instanceof ConverterEntity be) {
                    be.updateCasingChain(pPos.above());
                }
                break;
            }
            pos = pos.above();
            block = pLevel.getBlockState(pos);
        }

        pos = pPos.below();
        block = pLevel.getBlockState(pos);
        while (true) {
            if (!block.is(casingBlock)) break;
            pLevel.setBlock(pos, casingBlock.defaultBlockState(), 2);

            pos = pos.below();
            block = pLevel.getBlockState(pos);
        }
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        fillBlocksBelow(pLevel, pPos);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pNewState.isAir()) return;
        emptyBlocksBelow(pLevel, pPos);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            ItemStack item = player.getItemInHand(hand);
            if (item.is(ModItems.WORM_FOOD.get())) {
                player.setItemInHand(hand, new ItemStack(Items.BOWL));
                level.setBlock(pos, ModBlocks.CONVERTER.get().defaultBlockState(), 2);
                fillBlocksBelow(level, pos.below());
                return ItemInteractionResult.CONSUME;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
