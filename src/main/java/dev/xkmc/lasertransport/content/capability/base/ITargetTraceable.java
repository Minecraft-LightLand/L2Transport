package dev.xkmc.lasertransport.content.capability.base;

import dev.xkmc.l2library.util.code.Wrappers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public interface ITargetTraceable {

	default <T> LazyOptional<T> getCapability(Capability<T> cap, BlockPos pos) {
		Level level = getThis().getLevel();
		if (level != null) {
			BlockEntity target = level.getBlockEntity(pos);
			if (target != null) {
				return target.getCapability(cap, getNearest(getThis().getBlockPos().subtract(pos)));
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

	default BlockEntity getThis() {
		return Wrappers.cast(this);
	}

}
