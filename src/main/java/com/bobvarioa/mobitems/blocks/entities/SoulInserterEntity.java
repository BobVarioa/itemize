package com.bobvarioa.mobitems.blocks.entities;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.blocks.attachments.ItemHandlerAttachment;
import com.bobvarioa.mobitems.helpers.EntityUtils;
import com.bobvarioa.mobitems.register.ModAttachments;
import com.bobvarioa.mobitems.register.ModBlockEntities;
import com.bobvarioa.mobitems.register.ModBlocks;
import com.bobvarioa.mobitems.register.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@EventBusSubscriber(modid = MobItems.MODID, bus = EventBusSubscriber.Bus.MOD)
public class SoulInserterEntity extends BlockEntity {

	public static class SoulInserterItemStackHandler extends ItemHandlerAttachment {
		public SoulInserterItemStackHandler(int i, IAttachmentHolder attachmentHolder) {
			super(i, attachmentHolder, ModAttachments.SOUL_INSERTER.get());
		}

		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			return stack.is(ModItems.MOB_SOUL.get());
		}
	}

	public SoulInserterEntity(BlockPos pPos, BlockState pBlockState) {
		super(ModBlockEntities.SOUL_INSERTER_ENTITY.get(), pPos, pBlockState);
	}

	public int timer = 0;

	@SubscribeEvent
	private static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			ModBlockEntities.SOUL_INSERTER_ENTITY.get(),
			(be, side) -> be.getData(ModAttachments.SOUL_INSERTER.get())
		);
	}
	
	@Override
	public void setRemoved() {
		if (!level.isClientSide) {
			level.invalidateCapabilities(worldPosition);
		}
		super.setRemoved();
	}

	private static final Vector3f BLUE = Vec3.fromRGB24(Mth.hsvToRgb(185f / 255f, 1.0f, 1.0f)).toVector3f();

	public void soulInsert() {
		if (level.getBlockEntity(worldPosition.above()) instanceof MobBlockEntity be) {
			int soul = 0;
			IItemHandler itemHandler = be.getData(ModAttachments.SOUL_INSERTER.get());
			ItemStack stack = itemHandler.extractItem(0, 16, false);
			if (!stack.isEmpty()) {
				soul = stack.getCount();
			}

			be.entityTag.putFloat(EntityUtils.SOUL_KEY, be.entityTag.getFloat(EntityUtils.SOUL_KEY) + soul);
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
