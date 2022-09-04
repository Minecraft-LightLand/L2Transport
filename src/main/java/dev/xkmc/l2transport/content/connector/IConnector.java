package dev.xkmc.l2transport.content.connector;

import dev.xkmc.l2transport.content.flow.IContentHolder;
import dev.xkmc.l2transport.content.flow.NetworkType;
import dev.xkmc.l2transport.content.tile.base.IRenderableConnector;
import dev.xkmc.l2transport.content.tile.client.TooltipBuilder;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.function.Predicate;

public interface IConnector extends NetworkType, IRenderableConnector {

	List<BlockPos> getAvailableTarget();

	void perform();

	void link(BlockPos pos);

	void removeIf(Predicate<BlockPos> o);

	void tick();

	boolean isReady();

	void refreshCoolDown(BlockPos target, boolean success, boolean simulate);

	<T> void addTooltips(TooltipBuilder list, IContentHolder<T> filter);

	@Override
	default boolean testConsumption(int avail, int c) {
		return true;
	}

	@Override
	default boolean shouldContinue(int available, int consumed, int size) {
		return provide(available, consumed, size) > 0;
	}

	@Override
	default int provide(int available, int consumed, int size) {
		return Math.max(0, available - consumed);
	}

}
