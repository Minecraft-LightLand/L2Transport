package dev.xkmc.l2transport.content.upgrades;

import net.minecraft.world.item.Item;

public class UpgradeItem extends Item {

	private final Upgrade upgrade;

	public UpgradeItem(Properties props, Upgrade upgrade) {
		super(props);
		this.upgrade = upgrade;
	}

	public Upgrade getUpgrade() {
		return upgrade;
	}

	public UpgradeFlag getFlag() {
		return getUpgrade().getFlag();
	}
}
