package dev.xkmc.lasertransport.content.items.upgrades;

import dev.xkmc.lasertransport.init.data.LangData;
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
