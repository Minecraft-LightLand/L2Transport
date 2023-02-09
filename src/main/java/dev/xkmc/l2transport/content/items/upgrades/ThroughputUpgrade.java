package dev.xkmc.l2transport.content.items.upgrades;

import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.network.chat.MutableComponent;

public class ThroughputUpgrade extends Upgrade {

	private final int throughput;

	public ThroughputUpgrade(int throughput) {
		super(UpgradeFlag.THROUGH_PUT);
		this.throughput = throughput;
	}

	@Override
	public long getMaxTransfer(long cd) {
		return cd * throughput;
	}

	@Override
	public MutableComponent getDesc() {
		return LangData.UP_THROUGHPUT.get(throughput);
	}

}
