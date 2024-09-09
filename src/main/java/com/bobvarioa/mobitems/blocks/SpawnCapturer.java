package com.bobvarioa.mobitems.blocks;

import com.bobvarioa.mobitems.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SpawnCapturer extends Block implements EntityBlock {
    public SpawnCapturer(Properties pProperties) {
        super(pProperties);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ModBlockEntities.SPAWN_CAPTURER_ENTITY.get().create(blockPos, blockState);
    }
}
