package com.bobvarioa.mobitems.helpers;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class ItemHandlerProxy implements IItemHandler {
	
	private IItemHandler parent;
	private int from;
	private int to;
	
	public ItemHandlerProxy(IItemHandler handler) {
		this(handler, 0, handler.getSlots() - 1);
	}
	public ItemHandlerProxy(IItemHandler handler, int from, int to) {
		this.parent = handler;
		this.from = from;
		this.to = to;
	}
	
	@Override
	public int getSlots() {
		return to - from + 1;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return parent.getStackInSlot(i + from);
	}

	@Override
	public ItemStack insertItem(int i, ItemStack itemStack, boolean b) {
		return parent.insertItem(i + from, itemStack, b);
	}

	@Override
	public ItemStack extractItem(int i, int i1, boolean b) {
		return parent.extractItem(i + from, i1, b);
	}

	@Override
	public int getSlotLimit(int i) {
		return parent.getSlotLimit(i + from);
	}

	@Override
	public boolean isItemValid(int i, ItemStack itemStack) {
		return parent.isItemValid(i + from, itemStack);
	}
}
