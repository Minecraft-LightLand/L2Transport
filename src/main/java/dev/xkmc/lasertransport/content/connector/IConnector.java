package dev.xkmc.lasertransport.content.connector;

import dev.xkmc.lasertransport.content.flow.NetworkType;
import dev.xkmc.lasertransport.content.tile.base.IRenderableConnector;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.function.Predicate;

public interface IConnector extends NetworkType, IRenderableConnector {

	List<BlockPos> getAvailableTarget();

	void perform();

	boolean link(BlockPos pos);

	void removeIf(Predicate<BlockPos> o);

	void tick();

	boolean isReady();

	void refreshCoolDown(BlockPos target, boolean success, boolean simulate);

	@Override
	default boolean testConsumption(long avail, long c) {
		return true;
	}

	@Override
	default boolean shouldContinue(long available, long consumed, long size) {
		return provide(available, consumed, size) > 0;
	}

	@Override
	default long provide(long available, long consumed, long size) {
		return Math.max(0, available - consumed);
	}

}
