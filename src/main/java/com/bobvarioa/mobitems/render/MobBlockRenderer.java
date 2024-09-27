package com.bobvarioa.mobitems.render;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.blocks.entities.MobBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import static com.bobvarioa.mobitems.register.ModBlockEntities.MOB_BLOCK_ENTITY;

@EventBusSubscriber(value = Dist.CLIENT, modid = MobItems.MODID, bus = EventBusSubscriber.Bus.MOD)
public class MobBlockRenderer implements BlockEntityRenderer<MobBlockEntity> {
    private static MobBlockRenderer instance;

    public static MobBlockRenderer getInstance() {
        if (instance == null) {
            instance = new MobBlockRenderer();
        }
        return instance;
    }

    @Override
    public void render(MobBlockEntity be, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int pPackedLight, int pPackedOverlay) {
        BaseMobItemRenderer.render(be.entityTag, be.rotationDegrees, ItemDisplayContext.FIXED, poseStack, multiBufferSource, pPackedLight, pPackedOverlay);
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderer(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(MOB_BLOCK_ENTITY.get(), (ctx) -> MobBlockRenderer.getInstance());
    }
}