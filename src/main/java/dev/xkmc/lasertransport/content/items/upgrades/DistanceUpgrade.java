package dev.xkmc.lasertransport.content.items.upgrades;

import dev.xkmc.lasertransport.init.data.LangData;
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
