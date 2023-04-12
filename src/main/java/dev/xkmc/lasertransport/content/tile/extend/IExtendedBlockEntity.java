package dev.xkmc.lasertransport.content.tile.extend;

import dev.xkmc.lasertransport.content.capability.base.ITargetTraceable;
import dev.xkmc.lasertransport.content.capability.wrapper.ICapabilityHolder;
import dev.xkmc.lasertransport.content.tile.base.CoolDownType;
import dev.xkmc.lasertransport.content.tile.base.IRenderableConnector;
import dev.xkmc.lasertransport.content.tile.base.IRenderableNode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public interface IExtendedBlockEntity extends IRenderableNode, IRenderableConnector, ITargetTraceable {

	@Nullable
	BlockPos getTarget();

	@Nullable
	Level getTargetLevel();

	default boolean forceLoad() {
		return true;
	}

	@Override
	default List<BlockPos> getVisibleConnection() {
		BlockPos target = getTarget();
		Level targetLevel = getTargetLevel();
		if (getThis().getLevel() == null ||
				targetLevel != getThis().getLevel() ||
				target == null ||
				target.distSqr(getThis().getBlockPos()) > 256 // TODO config
		) return List.of();
		return List.of(target);
	}

	@Override
	default IRenderableConnector getConnector() {
		return this;
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

	<C> LazyOptional<C> getCapabilityOneStep(ICapabilityHolder<C> cap);

	static <C> LazyOptional<C> getCapabilityImpl(IExtendedBlockEntity self, ICapabilityHolder<C> cap) {
		BlockPos targetPos = self.getTarget();
		Level targetLevel = self.getTargetLevel();
		Level selfLevel = self.getThis().getLevel();
		boolean loaded = self.forceLoad() || targetLevel != null && targetPos != null && targetLevel.isLoaded(targetPos);
		if (selfLevel != null && targetLevel != null && targetPos != null && loaded) {
			BlockEntity be = targetLevel.getBlockEntity(targetPos);
			if (be != null) {
				if (be instanceof IExtendedBlockEntity target) {
					Set<MultiLevelTarget> set = new TreeSet<>();
					set.add(MultiLevelTarget.of(selfLevel, self.getThis().getBlockPos()));
					set.add(MultiLevelTarget.of(targetLevel, targetPos));
					return recursiveCap(target, cap, set);
				}
				return self.getCapabilityOneStep(cap);
			}
		}
		return LazyOptional.empty();
	}

	static <C> LazyOptional<C> recursiveCap(IExtendedBlockEntity self, ICapabilityHolder<C> cap, Set<MultiLevelTarget> set) {
		if (self.getThis().getLevel() != null) {
			BlockPos targetPos = self.getTarget();
			Level targetLevel = self.getTargetLevel();
			boolean loaded = self.forceLoad() || targetLevel != null && targetPos != null && targetLevel.isLoaded(targetPos);
			if (targetPos == null || targetLevel == null || !loaded ||
					set.contains(MultiLevelTarget.of(targetLevel, targetPos)))
				return LazyOptional.empty();
			BlockEntity be = self.getThis().getLevel().getBlockEntity(targetPos);
			if (be != null) {
				if (be instanceof IExtendedBlockEntity target) {
					set.add(MultiLevelTarget.of(targetLevel, targetPos));
					return recursiveCap(target, cap, set);
				}
				return self.getCapabilityOneStep(cap);
			}
		}
		return LazyOptional.empty();
	}

}
