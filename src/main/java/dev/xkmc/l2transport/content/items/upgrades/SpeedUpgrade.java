package dev.xkmc.l2transport.content.items.upgrades;

import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.network.chat.MutableComponent;

public class SpeedUpgrade extends Upgrade {

	private final int speed;

	public SpeedUpgrade(int speed) {
		super(UpgradeFlag.COOL_DOWN);
		this.speed = speed;
	}

	@Override
	public int getMaxCoolDown(int cd) {
		return cd / speed;
	}

	@Override
	public MutableComponent getDesc() {
		return LangData.UP_SPEED.get(speed);
	}

}
