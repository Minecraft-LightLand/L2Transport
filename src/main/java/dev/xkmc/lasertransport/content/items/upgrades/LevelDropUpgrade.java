package dev.xkmc.lasertransport.content.items.upgrades;

import dev.xkmc.lasertransport.init.data.LangData;
import net.minecraft.network.chat.MutableComponent;

public class LevelDropUpgrade extends Upgrade {

	public LevelDropUpgrade() {
		super(UpgradeFlag.LEVEL);
	}

	@Override
	public MutableComponent getDesc() {
		return LangData.UP_ENTITY.get();
	}

}
