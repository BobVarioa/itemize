package com.bobvarioa.mobitems.gui.menu.slots;

import com.bobvarioa.mobitems.gui.menu.MobModifierMenu;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class IconSlot extends SlotItemHandler {
	public static final ResourceLocation BLOCK_ATLAS = ResourceLocation.withDefaultNamespace("textures/atlas/blocks.png");
	
	private final ResourceLocation icon;

	public IconSlot(ResourceLocation icon, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.icon = icon;
	}

	@Nullable
	@Override
	public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
		return Pair.of(BLOCK_ATLAS, icon);
	}
}
