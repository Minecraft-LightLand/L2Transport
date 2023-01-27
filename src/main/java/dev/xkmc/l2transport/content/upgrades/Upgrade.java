package dev.xkmc.l2transport.content.upgrades;

public abstract class Upgrade {

	private final UpgradeFlag flag;

	public Upgrade(UpgradeFlag flag) {
		this.flag = flag;
	}

	public UpgradeFlag getFlag() {
		return flag;
	}

	public int getMaxCoolDown(int cd) {
		return cd;
	}

	public int getMaxDistance(int distSqr) {
		return distSqr;
	}

	public int getMaxTransfer(int amount) {
		return amount;
	}
}
