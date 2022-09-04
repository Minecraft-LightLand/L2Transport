package dev.xkmc.l2transport.content.connector;

import dev.xkmc.l2transport.content.flow.IContentHolder;
import dev.xkmc.l2transport.content.flow.NetworkType;
import dev.xkmc.l2transport.content.tile.base.CoolDownType;
import dev.xkmc.l2transport.content.tile.client.TooltipBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;
import java.util.TreeMap;
import java.util.function.Predicate;

public interface Connector extends NetworkType {

	List<BlockPos> getConnected();

	List<BlockPos> getAvailableTarget();

	void perform();

	void link(BlockPos pos);

	void removeIf(Predicate<BlockPos> o);

	int getMaxCoolDown(BlockPos pos);

	int getCoolDown(BlockPos pos);

	void tick();

	boolean isReady();

	void refreshCoolDown(BlockPos target, boolean success, boolean simulate);

	CoolDownType getType(BlockPos pos);

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
