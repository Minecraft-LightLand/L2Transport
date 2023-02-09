package dev.xkmc.l2transport.content.configurables;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;
import dev.xkmc.l2transport.content.client.overlay.TooltipBuilder;
import dev.xkmc.l2transport.content.client.overlay.TooltipType;
import dev.xkmc.l2transport.init.data.LangData;

import java.util.List;

@SerialClass
public abstract class CommonConfigurable<T> extends BaseConfigurable {

	public CommonConfigurable(ConfigConnectorType type, INodeBlockEntity node) {
		super(type, node);
	}

	@SerialClass.SerialField(toClient = true)
	protected boolean match_tag = false;

	protected abstract List<T> getFilters();

	protected abstract boolean match(T stack);

	public boolean isStackValid(T stack) {
		if (getFilters().isEmpty()) return true;
		return match(stack) == whitelist;
	}

	public boolean shouldDisplay() {
		return !getFilters().isEmpty();
	}

	public void clearFilter() {
		if (isLocked()) return;
		getFilters().clear();
		setTransferLimit(-1);
		whitelist = true;
	}

	public boolean hasNoFilter() {
		return getFilters().isEmpty();
	}

	public void setSimpleFilter(T copy) {
		if (isLocked()) return;
		clearFilter();
		getFilters().add(copy);
	}

	@Override
	public void addTooltips(TooltipBuilder list) {
		super.addTooltips(list);
		if (match_tag) {
			list.add(TooltipType.FILTER, LangData.CONFIG_TAG.get());
		}
	}

}
