package dev.xkmc.l2transport.content.items.upgrades;

import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.network.chat.MutableComponent;

public class ValveUpgrade extends Upgrade {

	public ValveUpgrade() {
		super(UpgradeFlag.REDSTONE);
	}

	@Override
	public MutableComponent getDesc() {
		return LangData.UP_VALVE.get();
	}

}