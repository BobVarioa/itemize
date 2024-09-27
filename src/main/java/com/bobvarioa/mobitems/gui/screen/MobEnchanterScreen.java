package com.bobvarioa.mobitems.gui.screen;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.gui.menu.MobEnchanterMenu;
import com.bobvarioa.mobitems.gui.menu.MobModifierMenu;
import com.bobvarioa.mobitems.items.MobItem;
import com.bobvarioa.mobitems.items.components.MobEnchantmentList;
import com.bobvarioa.mobitems.register.ModBehaviors;
import com.bobvarioa.mobitems.register.ModDataComponents;
import com.bobvarioa.mobitems.render.BaseMobItemRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MobEnchanterScreen extends AbstractContainerScreen<MobEnchanterMenu> {
	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MobItems.MODID, "textures/gui/container/mob_enchanter.png");

	public MobEnchanterScreen(MobEnchanterMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
		this.inventoryLabelY += 12;
		this.imageHeight = 174;
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

		MobEnchanterMenu menu = this.getMenu();
		ItemStack mob = menu.getSlot(0).getItem();

		var enchants = mob.get(ModDataComponents.MOB_ENCHANTMENTS.get());
		if (enchants != null) {
			int i = 0;
			for (var enchant : enchants.toList()) {
				var id = enchant.id();
				if (!id.equals(ModBehaviors.EMPTY.getId())) {
					var sprite = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "mob_enchantment/" + id.getPath());
					int x = this.leftPos + 53 + 37 * i;
					int y = this.topPos + 29;
					guiGraphics.blitSprite(sprite, x, y, 32, 32);

					if (mouseX > x && mouseX < x + 32 && mouseY > y && mouseY < y + 32) {
						List<Component> list = new ArrayList<>();
						list.add(Component.translatable(id.toLanguageKey("tooltip.mob_enchantment")));
						list.add(Component.translatable(id.toLanguageKey("tooltip.mob_enchantment", "description")));
						guiGraphics.renderTooltip(Minecraft.getInstance().font, list, Optional.<TooltipComponent>empty(), mouseX, mouseY);
					}
				}
				i++;
			}
		}

		this.renderTooltip(guiGraphics, mouseX, mouseY);

	}
}
