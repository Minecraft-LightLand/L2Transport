package dev.xkmc.l2transport.content.upgrades;

import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.network.chat.MutableComponent;

public class WatchUpgrade extends Upgrade {

	public WatchUpgrade() {
		super(UpgradeFlag.REDSTONE);
	}

	@Override
	public MutableComponent getDesc() {
		return LangData.UP_WATCH.get();
	}

}
