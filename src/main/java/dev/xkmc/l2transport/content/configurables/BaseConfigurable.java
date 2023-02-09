package dev.xkmc.l2transport.content.configurables;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;
import dev.xkmc.l2transport.content.client.overlay.TooltipBuilder;
import dev.xkmc.l2transport.content.client.overlay.TooltipType;
import dev.xkmc.l2transport.content.items.upgrades.UpgradeFlag;
import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.network.chat.MutableComponent;

@SerialClass
public abstract class BaseConfigurable {

	protected final ConfigConnectorType type;
	protected final INodeBlockEntity node;

	@SerialClass.SerialField(toClient = true)
	private long max_transfer = -1;

	@SerialClass.SerialField(toClient = true)
	private boolean locked = false;

	@SerialClass.SerialField(toClient = true)
	protected boolean whitelist = true;

	public BaseConfigurable(ConfigConnectorType type, INodeBlockEntity node) {
		this.type = type;
		this.node = node;
	}

	protected abstract int getTypeDefaultMax();

	public long getMaxTransfer() {
		long max = getTypeDefaultMax();
		var up = node.getUpgrade(UpgradeFlag.THROUGH_PUT);
		if (up != null) {
			max = up.getMaxTransfer(max);
		}
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

	public void setTransferLimit(int count) {
		if (locked) return;
		max_transfer = count;
	}

	public boolean allowExtract(long count) {
		return max_transfer <= 0 ? count <= getMaxTransfer() : count == max_transfer;
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

	public abstract MutableComponent getFilterDesc();

	public abstract boolean shouldDisplay();

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public ConfigConnectorType getType() {
		return type;
	}

}
