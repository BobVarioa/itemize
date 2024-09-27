package com.bobvarioa.mobitems.gui.screen;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.gui.menu.MobColosseumMenu;
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

public class MobColosseumScreen extends AbstractContainerScreen<MobColosseumMenu> {
	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MobItems.MODID, "textures/gui/container/mob_colosseum.png");
	private static final ResourceLocation ATTACK_SPRITE = ResourceLocation.fromNamespaceAndPath(MobItems.MODID, "mob_colosseum/attack");
	private static final ResourceLocation DEFEND_SPRITE = ResourceLocation.fromNamespaceAndPath(MobItems.MODID, "mob_colosseum/defend");
	private static final ResourceLocation PROGRESS_SPRITE = ResourceLocation.fromNamespaceAndPath(MobItems.MODID, "mob_colosseum/progress");

	public MobColosseumScreen(MobColosseumMenu menu, Inventory playerInventory, Component title) {
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

		MobColosseumMenu menu = this.getMenu();
		ItemStack attackerMob = menu.getSlot(0).getItem();
		ItemStack defenderMob = menu.getSlot(1).getItem();
		
		PoseStack pose = guiGraphics.pose();
		if (!attackerMob.isEmpty()) {
			CompoundTag tag = MobItem.getEntityData(attackerMob);
			
			pose.pushPose();
			pose.translate(this.leftPos + 6, this.topPos + 8 + 18 * 3, 150f);
			pose.scale(36.0F, -36.0F, 36.0F);
			BaseMobItemRenderer.render(tag, 45, ItemDisplayContext.NONE, pose, guiGraphics.bufferSource(),15728880, OverlayTexture.NO_OVERLAY);
			pose.popPose();
			
			int health = Math.min(18, (int)Math.ceil((tag.getFloat("Health") / tag.getFloat("MaxHealth")) * 18.0f));
			guiGraphics.blitSprite(ATTACK_SPRITE, 18, 18, 0,  18 - health, this.leftPos + 8 + 17, this.topPos + 72 + 18 - health, 18, health);
		}
		if (!defenderMob.isEmpty()) {
			CompoundTag tag = MobItem.getEntityData(defenderMob);

			pose.pushPose();
			pose.translate(this.leftPos + 6 + 18 * 5 + 36, this.topPos + 8 + 18 * 3, 150f);
			pose.scale(36.0F, -36.0F, 36.0F);
			BaseMobItemRenderer.render(tag, -45, ItemDisplayContext.NONE, pose, guiGraphics.bufferSource(),15728880, OverlayTexture.NO_OVERLAY);
			pose.popPose();

			int health = Math.min(18, (int)Math.ceil((tag.getFloat("Health") / tag.getFloat("MaxHealth")) * 18.0f));
			guiGraphics.blitSprite(DEFEND_SPRITE, 18, 18, 0,  18 - health, this.leftPos + 8 + 17 + 5 * 18 + 18, this.topPos + 72 + 18 - health, 18, health);
		}

		int currentProgress = menu.progress.get();
		int maxProgress = 20 * 3;
		int progress = (int)Math.ceil(((float)currentProgress / maxProgress) * 48.0f);
		guiGraphics.blitSprite(PROGRESS_SPRITE, 48, 5,  0, 0, this.leftPos + 8 + 20 + 36, this.topPos + 72 + 8, progress, 5);

		this.renderTooltip(guiGraphics, mouseX, mouseY);
		
	}
}
