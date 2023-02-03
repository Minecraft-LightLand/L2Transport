package dev.xkmc.l2transport.content.capability.base;

import dev.xkmc.l2transport.content.connector.IConnector;
import net.minecraft.core.BlockPos;

public interface INodeBlockEntity extends ITargetTraceable {

	IConnector getConnector();

	void refreshCoolDown(BlockPos target, boolean success, boolean simulate);

	default boolean isReady() {
		return getConnector().isReady() && getThis().getLevel() != null && !getThis().getLevel().hasNeighborSignal(getThis().getBlockPos());
	}

}
