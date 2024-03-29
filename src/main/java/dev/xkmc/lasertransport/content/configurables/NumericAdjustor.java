package dev.xkmc.lasertransport.content.configurables;

import dev.xkmc.lasertransport.init.LaserTransport;
import dev.xkmc.lasertransport.init.data.LTModConfig;
import dev.xkmc.lasertransport.network.SetNumberToServer;
import net.minecraft.core.BlockPos;

public record NumericAdjustor(NumericConfigurator scale, BaseConfigurable config) {

	private static BlockPos currentTarget = null;
	private static long targetValue = 0;
	private static int timeSince = 0;

	public static void flush() {
		if (currentTarget == null) return;
		LaserTransport.HANDLER.toServer(new SetNumberToServer(currentTarget, targetValue));
		currentTarget = null;
		timeSince = 0;
		targetValue = 0;
	}

	public static void tick() {
		if (currentTarget == null) {
			return;
		}
		timeSince++;
		if (timeSince >= LTModConfig.CLIENT.scrollDelay.get()) {
			flush();
		}
	}

	public long getTransferDisplay() {
		return Math.max(0, config.getTransferDisplay());
	}

	public long getMaxFilter() {
		return config.getMaxFilter();
	}

	public void setVal(long val) {
		if (config().isLocked()) return;
		BlockPos target = config.node.getThis().getBlockPos();
		if (currentTarget != null && !target.equals(currentTarget)) {
			flush();
		}
		currentTarget = target;
		targetValue = val;
		timeSince = 0;
		config().setTransferLimitDisplay(targetValue);
	}

}
