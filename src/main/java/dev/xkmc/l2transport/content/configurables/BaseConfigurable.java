package dev.xkmc.l2transport.content.configurables;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;
import dev.xkmc.l2transport.content.upgrades.UpgradeFlag;

@SerialClass
public abstract class BaseConfigurable {

	protected final ConfigConnectorType type;
	protected final INodeBlockEntity node;

	@SerialClass.SerialField(toClient = true)
	private long filterGate;

	public BaseConfigurable(ConfigConnectorType type, INodeBlockEntity node) {
		this.type = type;
		this.node = node;
		filterGate = -1;
	}

	protected abstract int getTypeDefaultMax();

	public long getMaxTransfer() {
		long max = getTypeDefaultMax();
		var up = node.getUpgrade(UpgradeFlag.THROUGH_PUT);
		if (up != null) {
			max = up.getMaxTransfer(max);
		}
		return filterGate <= 0 ? max : Math.min(max, filterGate);
	}

	public void setMaxTransfer(int count) {
		filterGate = count;
	}

	public boolean allowExtract(long count) {
		return type != ConfigConnectorType.EXTRACT || filterGate <= 0 ? count <= getMaxTransfer() : count >= filterGate;
	}


}
