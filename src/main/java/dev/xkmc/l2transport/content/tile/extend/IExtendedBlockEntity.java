package dev.xkmc.l2transport.content.tile.extend;

import dev.xkmc.l2transport.content.capability.base.ITargetTraceable;
import dev.xkmc.l2transport.content.tile.base.CoolDownType;
import dev.xkmc.l2transport.content.tile.base.IRenderableConnector;
import dev.xkmc.l2transport.content.tile.base.IRenderableNode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public interface IExtendedBlockEntity extends IRenderableNode, IRenderableConnector, ITargetTraceable {

	@Nullable
	BlockPos getTarget();

	@Override
	default List<BlockPos> getVisibleConnection() {
		BlockPos target = getTarget();
		return target == null ? List.of() : List.of(target);
	}

	@Override
	default IRenderableConnector getConnector() {
		return this;
	}

	@Override
	default boolean isTargetValid(BlockPos pos) {
		return getThis().getLevel() != null && getThis().getLevel().getBlockEntity(pos) != null;
	}

	@Override
	default int getCoolDown(BlockPos target) {
		return 0;
	}

	@Override
	default int getMaxCoolDown(BlockPos target) {
		return 1;
	}

	@Override
	default CoolDownType getType(BlockPos target) {
		return CoolDownType.GREEN;
	}

	<C> LazyOptional<C> getCapabilityOneStep(Capability<C> cap);

	static <C> LazyOptional<C> getCapabilityImpl(IExtendedBlockEntity self, Capability<C> cap) {
		BlockPos pos = self.getTarget();
		if (self.getThis().getLevel() != null && pos != null) {
			BlockEntity be = self.getThis().getLevel().getBlockEntity(pos);
			if (be != null) {
				if (be instanceof IExtendedBlockEntity target) {
					Set<BlockPos> set = new TreeSet<>();
					set.add(self.getThis().getBlockPos());
					set.add(pos);
					return recursiveCap(target, cap, set);
				}
				return self.getCapabilityOneStep(cap);
			}
		}
		return LazyOptional.empty();
	}

	static <C> LazyOptional<C> recursiveCap(IExtendedBlockEntity self, Capability<C> cap, Set<BlockPos> set) {
		if (self.getThis().getLevel() != null) {
			BlockPos pos = self.getTarget();
			if (pos == null || set.contains(pos)) return LazyOptional.empty();
			BlockEntity be = self.getThis().getLevel().getBlockEntity(pos);
			if (be != null) {
				if (be instanceof IExtendedBlockEntity target) {
					set.add(pos);
					return recursiveCap(target, cap, set);
				}
				return self.getCapabilityOneStep(cap);
			}
		}
		return LazyOptional.empty();
	}

}
