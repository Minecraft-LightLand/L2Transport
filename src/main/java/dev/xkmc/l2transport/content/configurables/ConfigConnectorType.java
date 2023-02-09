package dev.xkmc.l2transport.content.configurables;

public enum ConfigConnectorType {
	SIMPLE(true),
	EXTRACT(true),
	DISTRIBUTE(false),
	ORDERED(false),
	SYNC(true);

	private final boolean canSetCount;

	ConfigConnectorType(boolean canSetCount) {
		this.canSetCount = canSetCount;
	}

	public boolean canSetCount() {
		return canSetCount;
	}

}
