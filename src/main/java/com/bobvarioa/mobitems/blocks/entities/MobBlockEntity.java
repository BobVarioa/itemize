package com.bobvarioa.mobitems.blocks.entities;

import com.bobvarioa.mobitems.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MobBlockEntity extends BlockEntity {
    public CompoundTag entityTag = new CompoundTag();
    public float rotationDegrees = 0;
	public boolean destroyHandled = false;

    public MobBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.MOB_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("EntityTag", this.entityTag);
        tag.putFloat("rotation", this.rotationDegrees);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.entityTag = tag.getCompound("EntityTag");
        this.rotationDegrees = tag.getFloat("rotation");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
        CompoundTag tag = pkt.getTag();
        if (tag == null) {
            tag = new CompoundTag();
        }
        loadAdditional(tag, lookupProvider);
    }
}
