package com.bobvarioa.mobitems.blocks.entities;

import com.bobvarioa.mobitems.helpers.EntityUtils;
import com.bobvarioa.mobitems.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;

import java.util.LinkedHashSet;
import java.util.Set;

public class SpawnCapturerEntity extends BlockEntity {
    public static Set<SpawnCapturerEntity> activeSpawnCapturers = new LinkedHashSet<>();

    public SpawnCapturerEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SPAWN_CAPTURER_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide) activeSpawnCapturers.add(this);
    }

    @Override
    public void onChunkUnloaded() {
        activeSpawnCapturers.remove(this);
        super.onChunkUnloaded();
    }

    @Override
    public void setRemoved() {
        activeSpawnCapturers.remove(this);
        super.setRemoved();
    }

    public void captureEntity(LivingEntity entity) {
        ItemStack item = EntityUtils.entityToItem(entity);
        Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), item);
        if (!entity.isRemoved()) entity.remove(Entity.RemovalReason.DISCARDED);
    }

    public static boolean onEntitySpawn(MobSpawnEvent.PositionCheck event) {
        Mob entity = event.getEntity();
        if (entity.level().isClientSide) return true;

        boolean shouldCapture = switch (event.getSpawnType()) {
            case NATURAL, EVENT, JOCKEY, PATROL, REINFORCEMENT, MOB_SUMMONED, TRIGGERED -> true;
            default -> false;
        };

        if (!shouldCapture) return true;

        var entityPos = entity.chunkPosition();
        DimensionType dimensionType = entity.level().dimensionType();
        for (SpawnCapturerEntity be : activeSpawnCapturers) {
            if (be.level == null) continue;
            if (dimensionType != be.level.dimensionType()) continue;

            var chunkPosX = be.worldPosition.getX() >> 4;
            var chunkPosZ = be.worldPosition.getZ() >> 4;

            var xDist = Math.abs(chunkPosX - entityPos.x);
            var zDist = Math.abs(chunkPosZ - entityPos.z);

            if (xDist <= 6 || zDist <= 6) {
                if (!be.level.hasNeighborSignal(be.worldPosition)) {
					if (be.level.getRandom().nextIntBetweenInclusive(1, 10) <= 2) {
                    	be.captureEntity(entity);
					}
                    return false;
                }
            }
        }

        return true;
    }

}
