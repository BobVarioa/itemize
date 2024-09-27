package com.bobvarioa.mobitems.render;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.items.MobItem;
import com.bobvarioa.mobitems.register.ModItems;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.item.ItemProperties;
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
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
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
	
	public static class MobItemItemOverrides extends ItemOverrides {
		@Nullable
		@Override
		public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
			return model;
		}
	}
	
	public static class MobItemBakedModel extends BakedModelWrapper<BakedModel> {
		private final ItemOverrides overrides;
        public MobItemBakedModel(BakedModel originalModel) {
            super(originalModel);
			this.overrides = new MobItemItemOverrides();
        }
		

        @Override
        public boolean isCustomRenderer() {
            return true;
        }

        @Override
        public boolean isGui3d() {
            return true;
        }

		@Override
		public ItemOverrides getOverrides() {
			return overrides;
		}

		@Override
		public BakedModel applyTransform(ItemDisplayContext cameraTransformType, PoseStack poseStack, boolean applyLeftHandTransform) {
			return this;
		}
	}
	
	public static void clientInit(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
        	ItemProperties.register(MOB_ITEM.get(), MOB_ITEM.getId(), getInstance());
		});
    }
    @SubscribeEvent
    public static void registerAdditional(ModelEvent.ModifyBakingResult event) {
        var location = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath("mobitems", "mob_item"), "inventory");
        var oldModel = event.getModels().get(location);
		
		event.getModels().put(location, new MobItemBakedModel(oldModel));
    }

    @Override
    public float call(ItemStack pStack, @Nullable ClientLevel pLevel, @Nullable LivingEntity pEntity, int pSeed) {
        return 1;
    }
    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        var tag = MobItem.getEntityData(stack);
        BaseMobItemRenderer.render(tag, 0, displayContext, poseStack, buffer, packedLight, packedOverlay);
    }

}
