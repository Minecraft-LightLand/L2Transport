package dev.xkmc.l2transport.content.capability.base;

import dev.xkmc.l2transport.content.connector.IConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public interface INodeBlockEntity {

	IConnector getConnector();

	@Nullable
	Level getLevel();

	void refreshCoolDown(BlockPos target, boolean success, boolean simulate);

	BlockPos getBlockPos();

	default boolean isReady() {
		return getConnector().isReady() && getLevel() != null && getLevel().hasNeighborSignal(getBlockPos());
	}

	default <T> LazyOptional<T> getCapability(Capability<T> cap, BlockPos pos) {
		Level level = getLevel();
		if (level != null) {
			BlockEntity target = level.getBlockEntity(pos);
			if (target != null) {
				return target.getCapability(cap, getNearest(getBlockPos().subtract(pos)));
			}
		}
		return LazyOptional.empty();
	}

	@Nullable
	static Direction getNearest(BlockPos pos) {
		Direction ans = Direction.NORTH;
		int max = Integer.MIN_VALUE;
		int count = 0;

		for (Direction dire : Direction.values()) {
			int val = pos.getX() * dire.getNormal().getX() +
					pos.getY() * dire.getNormal().getY() +
					pos.getZ() * dire.getNormal().getZ();
			if (val == max) {
				count++;
			} else if (val > max) {
				max = val;
				count = 1;
				ans = dire;
			}
		}
		return count == 1 ? ans : null;
	}

}
