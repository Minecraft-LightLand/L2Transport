package dev.xkmc.l2transport.content.item;

import dev.xkmc.l2transport.content.api.IContentToken;
import dev.xkmc.l2transport.content.api.RealToken;
import dev.xkmc.l2transport.content.core.AbstractNodeTarget;
import dev.xkmc.l2transport.init.L2Transport;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemNodeTarget extends AbstractNodeTarget<ItemStack> {

	private static int computeConsumption(IItemHandler handler, IContentToken<ItemStack> token) {
		ItemStack avail = token.getRemain();
		int count = avail.getCount();
		ItemStack remain = ItemHandlerHelper.insertItemStacked(handler, avail, true);
		return count - remain.getCount();
	}

	private final IItemHandler handler;

	public ItemNodeTarget(BlockEntity be, IItemHandler handler, IContentToken<ItemStack> token) {
		super(be, token, computeConsumption(handler, token));
		this.handler = handler;
	}

	@Override
	public void perform(RealToken<ItemStack> real) {
		ItemStack remain = ItemHandlerHelper.insertItemStacked(handler, real.split(consumed), false);
		if (!remain.isEmpty()) {
			real.gain(remain.getCount());
			L2Transport.LOGGER.error("Mismatch behavior for item insertion at " + be);
		}
	}

}
