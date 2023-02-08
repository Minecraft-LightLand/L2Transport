package dev.xkmc.l2transport.content.upgrades;

import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.network.chat.MutableComponent;

public class LevelPlaceUpgrade extends Upgrade {

	public LevelPlaceUpgrade() {
		super(UpgradeFlag.LEVEL);
	}

	@Override
	public MutableComponent getDesc() {
		return LangData.UP_PLACE.get();
	}

}
