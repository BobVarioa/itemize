package com.bobvarioa.mobitems.blocks.attachments;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ItemHandlerAttachment extends ItemStackHandler {
	private final IAttachmentHolder attachmentHolder;
	private final AttachmentType<ItemHandlerAttachment> type;

	public ItemHandlerAttachment(int i, IAttachmentHolder attachmentHolder, AttachmentType<? extends ItemHandlerAttachment> type) {
		super(i);
		this.attachmentHolder = attachmentHolder;
		this.type = (AttachmentType<ItemHandlerAttachment>) type;
	}

	@Override
	protected void onContentsChanged(int slot) {
		attachmentHolder.setData(type, this);
	}
}
