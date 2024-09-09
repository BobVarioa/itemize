package com.bobvarioa.mobitems.items;

import com.bobvarioa.mobitems.register.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class DustyMobSoul extends Item {
    public DustyMobSoul(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        BlockPos pos = entity.blockPosition();
        Level level = entity.level();
        if (level.getBlockState(pos).is(Blocks.WATER)) {
            entity.remove(Entity.RemovalReason.DISCARDED);
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.MOB_SOUL.get(), stack.getCount()));
        }
        return false;
    }
}
