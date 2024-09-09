package com.bobvarioa.mobitems.blocks;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.entity.EntityUtils;
import com.bobvarioa.mobitems.register.ModBlockEntities;
import com.bobvarioa.mobitems.register.ModBlocks;
import com.bobvarioa.mobitems.register.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

@EventBusSubscriber(modid = MobItems.MODID, bus = EventBusSubscriber.Bus.MOD)
public class SoulInserterEntity extends BlockEntity {

    public SoulInserterEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SOUL_INSERTER_ENTITY.get(), pPos, pBlockState);
    }

    public int timer = 0;

    public int progress = 0;


    @SubscribeEvent
    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlock(
                Capabilities.ItemHandler.BLOCK, // capability to register for
                (level, pos, state, be, side) -> new ItemStackHandler(1) {
                    @Override
                    protected void onContentsChanged(int slot) {
                        be.setChanged();
                    }

                    @Override
                    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                        return stack.is(ModItems.MOB_SOUL.get());
                    }
                },
                ModBlocks.SOUL_EXTRACTOR.get()
        );
    }


    public BlockCapabilityCache<IItemHandler, @Nullable Direction> itemHandlerCache;

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide) {
            itemHandlerCache = BlockCapabilityCache.create(
                    Capabilities.ItemHandler.BLOCK,
                    (ServerLevel) level,
                    worldPosition,
                    null,
                    () -> !this.isRemoved(),
                    () -> {}
            );
        }
    }

    @Override
    public void setRemoved() {
        level.invalidateCapabilities(worldPosition);
        super.setRemoved();
    }

    private static final Vector3f BLUE = Vec3.fromRGB24(Mth.hsvToRgb(185f / 255f, 1.0f, 1.0f)).toVector3f();


    public void soulInsert() {
        if (level.getBlockEntity(worldPosition.above()) instanceof MobBlockEntity be) {
            int soul = 0;
            IItemHandler itemHandler = itemHandlerCache.getCapability();
            if (itemHandler != null) {
                ItemStack stack = itemHandler.extractItem(0, 16, false);
                if (!stack.isEmpty()) {
                    soul = stack.getCount();
                }

                be.entityTag.putFloat(EntityUtils.SOUL_KEY, be.entityTag.getFloat(EntityUtils.SOUL_KEY) + soul);
            }
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        SoulInserterEntity be = (SoulInserterEntity) blockEntity;
        if (!level.isClientSide) {
            be.timer++;
            if (be.timer >= 20) {
                be.soulInsert();
                be.timer = 0;
            }
        } else {
            if (level.getGameTime() % 20 == 0 && level.getBlockState(pos.above()).is(ModBlocks.MOB_BLOCK.get())) {
                ParticleUtils.spawnParticleOnFace(level, pos.above(), Direction.UP, new DustParticleOptions(BLUE, 1.5f), new Vec3(0.0, 1.0, 0.0), 0.5f);
            }
        }
    }
}
