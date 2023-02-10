package dev.xkmc.l2transport.content.configurables;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.item.IItemNodeBlockEntity;
import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class ItemConfigurable extends CommonConfigurable<ItemStack> {

	@SerialClass.SerialField(toClient = true)
	private final ArrayList<ItemStack> filters = new ArrayList<>();

	@SerialClass.SerialField(toClient = true)
	protected boolean match_tag = false;

	public ItemConfigurable(ConfigConnectorType type, IItemNodeBlockEntity node) {
		super(type, node);
	}

	@Override
	protected List<ItemStack> getFilters() {
		return filters;
	}

	protected boolean match(ItemStack stack) {
		for (ItemStack filter : filters) {
			if (stack.getItem() == filter.getItem()) {
				if (match_tag) {
					if (ItemStack.isSameItemSameTags(stack, filter)) {
						return true;
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}

	public ItemStack getDisplayItem() {
		return filters.size() == 1 ? filters.get(0) : ItemStack.EMPTY;
	}

	public MutableComponent getFilterDesc() {
		return filters.size() == 1 ? filters.get(0).getHoverName().copy() : LangData.CONFIG_FILTER.get();
	}

	public boolean canQuickSetCount(ItemStack stack) {
		if (isLocked()) return false;
		return type.canSetCount() && shouldDisplay() && filters.size() == 1 && filters.get(0).getItem() == stack.getItem();
	}

	@Override
	protected int getTypeDefaultMax() {
		return 64;
	}

	@Override
	public NumericAdjustor getTransferConfig() {
		return new NumericAdjustor(NumericConfigurator.ITEM, this);
	}

}
