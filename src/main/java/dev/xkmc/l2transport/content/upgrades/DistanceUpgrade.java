package dev.xkmc.l2transport.content.upgrades;

import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.network.chat.MutableComponent;

public class DistanceUpgrade extends Upgrade {

	private final int dist;

	public DistanceUpgrade(int dist) {
		super(UpgradeFlag.DISTANCE);
		this.dist = dist;
	}

	@Override
	public int getMaxDistance(int cd) {
		return cd * dist;
	}

	@Override
	public MutableComponent getDesc() {
		return LangData.UP_DIST.get(dist);
	}

}
