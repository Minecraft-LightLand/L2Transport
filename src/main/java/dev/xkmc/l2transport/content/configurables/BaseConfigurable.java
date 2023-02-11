package dev.xkmc.l2transport.content.configurables;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;
import dev.xkmc.l2transport.content.client.overlay.TooltipBuilder;
import dev.xkmc.l2transport.content.client.overlay.TooltipType;
import dev.xkmc.l2transport.content.items.upgrades.UpgradeFlag;
import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.MenuProvider;

import javax.annotation.Nullable;

@SerialClass
public abstract class BaseConfigurable {

	protected final ConfigConnectorType type;
	protected final INodeBlockEntity node;

	@SerialClass.SerialField(toClient = true)
	private long max_transfer = -1;

	@SerialClass.SerialField(toClient = true)
	boolean locked = false;

	@SerialClass.SerialField(toClient = true)
	protected boolean whitelist = true;

	private long display_transfer = -1;
	protected boolean in_use = false;

	public BaseConfigurable(ConfigConnectorType type, INodeBlockEntity node) {
		this.type = type;
		this.node = node;
	}

	public INodeBlockEntity getNode() {
		return node;
	}

	public ConfigConnectorType getType() {
		return type;
	}

	public void addTooltips(TooltipBuilder list) {
		if (shouldDisplay()) {
			list.add(TooltipType.FILTER, (whitelist ? LangData.INFO_WHITELIST : LangData.INFO_BLACKLIST).get(getFilterDesc()));
		}
		long max = getMaxTransfer();
		if (type == ConfigConnectorType.EXTRACT) {
			long min = getFixedTransfer();
			if (min > 0) {
				list.add(TooltipType.GATE, LangData.INFO_EXTRACT.getLiteral(min));
			} else {
				list.add(TooltipType.GATE, LangData.INFO_GATED.getLiteral(max));
			}
		} else if (type == ConfigConnectorType.SYNC) {
			list.add(TooltipType.GATE, LangData.INFO_SYNC.getLiteral(getFixedTransfer()));
		} else {
			list.add(TooltipType.GATE, LangData.INFO_GATED.getLiteral(max));
		}
		if (locked) {
			list.add(TooltipType.NAME, LangData.CONFIG_LOCK.get());
		}
	}

	public abstract NumericAdjustor getTransferConfig();

	public abstract MutableComponent getFilterDesc();

	@Nullable
	public MenuProvider getMenu() {
		return null;
	}

	protected abstract int getTypeDefaultMax();

	public abstract boolean shouldDisplay();

	public boolean allowExtract(long count) {
		return max_transfer <= 0 ? count <= getMaxTransfer() : count == max_transfer;
	}

	long getMaxFilter() {
		long max = getTypeDefaultMax();
		var up = node.getUpgrade(UpgradeFlag.THROUGH_PUT);
		if (up != null) {
			max = up.getMaxTransfer(max);
		}
		return max;
	}

	public long getMaxTransfer() {
		long max = getMaxFilter();
		return max_transfer <= 0 ? max : Math.min(max, max_transfer);
	}

	public long getFixedTransfer() {
		if (type == ConfigConnectorType.EXTRACT) {
			if (max_transfer <= 0) return 0;
			return getMaxTransfer();
		}
		if (type == ConfigConnectorType.SYNC) {
			if (max_transfer <= 0) return 1;
			return getMaxTransfer();
		}
		return 0;
	}

	public void setTransferLimit(long count) {
		if (locked) return;
		max_transfer = count;
	}

	public boolean isLocked() {
		return locked || in_use;
	}

	long getTransferDisplay() {
		return display_transfer >= 0 ? display_transfer : max_transfer;
	}

	public void setTransferLimitDisplay(long targetValue) {
		display_transfer = targetValue;
	}

	public boolean isInUse() {
		return in_use;
	}

	public void setInUse(boolean use) {
		in_use = use;
	}


}
