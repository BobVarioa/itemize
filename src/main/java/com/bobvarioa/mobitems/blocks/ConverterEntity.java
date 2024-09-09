package com.bobvarioa.mobitems.blocks;

import com.bobvarioa.mobitems.entity.EntityUtils;
import com.bobvarioa.mobitems.items.MobItem;
import com.bobvarioa.mobitems.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

public class ConverterEntity extends BlockEntity {

    public ConverterEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CONVERTER_ENTITY.get(), pPos, pBlockState);
    }

    public int timer = 0;
    public int length = 1;

    public static int TWO_MINUTES = 60 * 2;

    public int hungerTime = TWO_MINUTES; // 2 minutes

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        length = tag.getInt("length");
        hungerTime = tag.getInt("hungerTime");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("length", length);
        tag.putInt("hungerTime", hungerTime);
    }

    public void updateCasingChain(BlockPos pos) {
        length = Math.abs(worldPosition.getY() - pos.getY()) + 1;
    }

    public void vacuumMobs() {
        Vec3 top = new Vec3(worldPosition.getX() + 0.5 - length, worldPosition.getY() + 2, worldPosition.getZ() + 0.5 - length);
        Vec3 bottom = new Vec3(worldPosition.getX() + 1 + length, worldPosition.getY() + 1, worldPosition.getZ() + 1 + length);

        AABB box = new AABB(top, bottom);

        boolean entitiesRemoved = false;
        List<Entity> entities = level.getEntities(null, box);
        for (Entity target : entities) {
            if (!(target instanceof Player) && target instanceof LivingEntity entity) {
                if (entity.getType().is(Tags.EntityTypes.BOSSES)) {
                    continue;
                }

                var item = EntityUtils.entityToItem(entity);

                if (hungerTime <= 0) {
                    var tag = MobItem.getEntityData(item);
                    int modifier = 1;
                    while (modifier < 9) {
                        if (hungerTime <= -TWO_MINUTES * modifier) {
                            modifier ++;
                        } else {
                            break;
                        }
                    }

                    float health = tag.getFloat("Health");
                    if (health >= 0.0f) {
                        tag.putFloat("Health", health - (health * (modifier / 10f)));
                        hungerTime += TWO_MINUTES * modifier;
                    }
                }

                BlockPos belowPos = worldPosition.offset(0, -1, 0);
                if (level.getBlockState(belowPos).isAir()) {
                    Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY() - 1, worldPosition.getZ(), item);
                } else {
                    Container cont = HopperBlockEntity.getContainerAt(level, belowPos);
                    if (cont != null) {
                        HopperBlockEntity.addItem(null, cont, item, Direction.DOWN);
                    } else {
                        Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), item);
                    }
                }
                entity.remove(Entity.RemovalReason.DISCARDED);
                entitiesRemoved = true;
            }
        }
        if (entitiesRemoved) {
            level.playLocalSound(worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.AMBIENT, 100.0f, 1.0f, false);
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        ConverterEntity be = (ConverterEntity) blockEntity;
        be.timer++;
        if (be.timer >= 20) {
            if (!level.hasNeighborSignal(pos)) {
                be.vacuumMobs();
                be.hungerTime -= be.length;
            }
            be.timer = 0;
        }
    }
}
