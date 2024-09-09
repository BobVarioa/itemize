package com.bobvarioa.mobitems.render;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.items.MobItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import org.jetbrains.annotations.Nullable;

import static com.bobvarioa.mobitems.MobItems.MODID;
import static com.bobvarioa.mobitems.register.ModItems.MOB_ITEM;

@EventBusSubscriber(value = Dist.CLIENT, modid = MobItems.MODID, bus = EventBusSubscriber.Bus.MOD)
public class MobItemRenderer extends BlockEntityWithoutLevelRenderer implements ItemPropertyFunction {
    private static MobItemRenderer instance;

    public static MobItemRenderer getInstance() {
        if (instance == null) {
            instance = new MobItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        }
        return instance;
    }

    public MobItemRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
    }

    public static class MobItemBakedModel extends BakedModelWrapper<BakedModel> {

        public MobItemBakedModel(BakedModel originalModel) {
            super(originalModel);
        }

        @Override
        public boolean isCustomRenderer() {
            return true;
        }

        @Override
        public boolean isGui3d() {
            return true;
        }
    }

    @Override
    public float call(ItemStack pStack, @Nullable ClientLevel pLevel, @Nullable LivingEntity pEntity, int pSeed) {
        return 1;
    }

    @SubscribeEvent
    public static void modifyBakingResult(ModelEvent.ModifyBakingResult event) {
        var location = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath("mobitems", "mob_item"), "inventory");
        event.getModels().put(location, new MobItemBakedModel(event.getModels().get(location)));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        var tag = MobItem.getEntityData(stack);
        BaseMobItemRenderer.render(tag, 0, displayContext, poseStack, buffer, packedLight, packedOverlay);
    }

}
