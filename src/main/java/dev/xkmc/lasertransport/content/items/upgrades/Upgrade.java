package dev.xkmc.lasertransport.content.items.upgrades;

import net.minecraft.network.chat.MutableComponent;

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

	public long getMaxTransfer(long amount) {
		return amount;
	}

	public abstract MutableComponent getDesc();

}
