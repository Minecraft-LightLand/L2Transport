package dev.xkmc.l2transport.content.upgrades;

import dev.xkmc.l2transport.init.data.LangData;
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
