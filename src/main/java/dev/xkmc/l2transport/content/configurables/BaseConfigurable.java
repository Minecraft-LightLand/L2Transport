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

	public void setMaxTransfer(int count) {
		if (locked) return;
		max_transfer = count;
	}

	public boolean allowExtract(long count) {
		return type != ConfigConnectorType.EXTRACT || max_transfer <= 0 ? count <= getMaxTransfer() : count >= max_transfer;
	}

	public void addTooltips(TooltipBuilder list) {
		if (shouldDisplay()) {
			list.add(TooltipType.FILTER, (whitelist ? LangData.INFO_WHITELIST : LangData.INFO_BLACKLIST).get(getFilterDesc()));
		}
		list.add(TooltipType.GATE, type.getInfo().getLiteral(getMaxTransfer()));
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
