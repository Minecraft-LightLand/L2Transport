package dev.xkmc.l2transport.content.configurables;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.item.IItemNodeBlockEntity;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

@SerialClass
public class ItemConfigurable extends BaseConfigurable implements IConfigurableFilter {

	@SerialClass.SerialField(toClient = true)
	private ItemStack filter = ItemStack.EMPTY;

	@SerialClass.SerialField(toClient = true)
	protected boolean match_tag = false;

	public ItemConfigurable(ConfigConnectorType type, IItemNodeBlockEntity node) {
		super(type, node);
	}

	public boolean isItemStackValid(ItemStack stack) {
		if (filter.isEmpty()) return true;
		if (match_tag) return ItemStack.isSameItemSameTags(stack, filter);
		return stack.getItem() == filter.getItem();
	}

	public ItemStack getDisplayItem() {
		return filter;
	}

	@Override
	public MutableComponent getFilterDesc() {
		return filter.getHoverName().copy();
	}

	@Override
	public boolean shouldDisplay() {
		return !filter.isEmpty();
	}

	public void clearFilter() {
		filter = ItemStack.EMPTY;
	}

	public boolean hasNoFilter() {
		return filter.isEmpty();
	}

	public void setSimpleFilter(ItemStack copy) {
		filter = copy;
	}

	public boolean canQuickSetCount(ItemStack stack) {
		return type.canSetCount() && shouldDisplay() && filter.getItem() == stack.getItem();
	}

	@Override
	protected int getTypeDefaultMax() {
		return 64;
	}

}
