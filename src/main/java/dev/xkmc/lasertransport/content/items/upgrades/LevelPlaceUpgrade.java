package dev.xkmc.lasertransport.content.items.upgrades;

import dev.xkmc.lasertransport.init.data.LangData;
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
