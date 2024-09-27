package com.bobvarioa.mobitems.gui.screen;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.gui.menu.MobModifierMenu;
import com.bobvarioa.mobitems.items.MobItem;
import com.bobvarioa.mobitems.render.BaseMobItemRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class MobModifierScreen extends AbstractContainerScreen<MobModifierMenu> {
	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MobItems.MODID, "textures/gui/container/mob_modifier.png");

	public MobModifierScreen(MobModifierMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
		this.inventoryLabelY += 22;
		this.imageHeight = 187;
		this.imageWidth = 176;
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	protected void containerTick() {
		super.containerTick();
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.render(guiGraphics, mouseX, mouseY, partialTick);

		MobModifierMenu menu = this.getMenu();
		ItemStack mob = menu.getSlot(0).getItem();
		
		PoseStack pose = guiGraphics.pose();
		if (!mob.isEmpty()) {
			CompoundTag tag = MobItem.getEntityData(mob);
			
			pose.pushPose();
			pose.translate(this.leftPos + 6 + 18 * 2, this.topPos + 8 + 18 * 3, 150f);
			pose.scale(36.0F, -36.0F, 36.0F);
			BaseMobItemRenderer.render(tag, 45, ItemDisplayContext.NONE, pose, guiGraphics.bufferSource(),15728880, OverlayTexture.NO_OVERLAY);
			pose.popPose();
		}
		
		this.renderTooltip(guiGraphics, mouseX, mouseY);
		
	}
}
