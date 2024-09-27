package com.bobvarioa.mobitems.blocks.entities;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.blocks.attachments.ItemHandlerAttachment;
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
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@EventBusSubscriber(modid = MobItems.MODID, bus = EventBusSubscriber.Bus.MOD)
public class SoulExtractorEntity extends BlockEntity {

	public static class SoulExtractorItemHandler extends ItemHandlerAttachment {
		public SoulExtractorItemHandler(int i, IAttachmentHolder attachmentHolder) {
			super(i, attachmentHolder, ModAttachments.SOUL_EXTRACTOR.get());
		}

		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			return false;
		}
	}

	public SoulExtractorEntity(BlockPos pPos, BlockState pBlockState) {
		super(ModBlockEntities.SOUL_EXTRACTOR_ENTITY.get(), pPos, pBlockState);
	}

	public int timer = 0;

	public int progress = 0;

	@SubscribeEvent
	private static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			ModBlockEntities.SOUL_EXTRACTOR_ENTITY.get(),
			(be, direction) -> be.getData(ModAttachments.SOUL_EXTRACTOR.get())
		);
	}
	

	@Override
	public void setRemoved() {
		if (level != null && !level.isClientSide) {
			level.invalidateCapabilities(worldPosition);
		}
		super.setRemoved();
	}

	private static final Vector3f RED = Vec3.fromRGB24(Mth.hsvToRgb(0f, 1.0f, 1.0f)).toVector3f();

	public void soulExtract() {
		if (level != null && level.getBlockEntity(worldPosition.above()) instanceof MobBlockEntity be) {
			float health = be.entityTag.getFloat("Health") - 0.2f;
			be.entityTag.putFloat("Health", health);
			if (health <= 0.0f) {
				level.destroyBlock(worldPosition.above(), false);
			}
			progress += 2;
			while (progress >= 50) {
				int count = 1;
				IItemHandlerModifiable itemHandler = this.getData(ModAttachments.SOUL_EXTRACTOR.get());
				var prev = itemHandler.getStackInSlot(0);
				if (!prev.isEmpty()) {
					count += prev.getCount();
				}
				if (count > 16) {
					break;
				}
				ItemStack stack = new ItemStack(ModItems.MOB_SOUL.get(), count);

				itemHandler.setStackInSlot(0, stack);
				progress -= 50;
			}
		}
	}

	public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
		SoulExtractorEntity be = (SoulExtractorEntity) blockEntity;
		if (!level.isClientSide) {
			be.timer++;
			if (be.timer >= 20) {
				be.soulExtract();
				be.timer = 0;
			}
		} else {
			if (level.getGameTime() % 20 == 0 && level.getBlockState(pos.above()).is(ModBlocks.MOB_BLOCK.get())) {
				ParticleUtils.spawnParticleOnFace(level, pos.above(), Direction.UP, new DustParticleOptions(RED, 1.5f), new Vec3(0.0, 1.0, 0.0), 0.5f);
			}
		}
	}
}
