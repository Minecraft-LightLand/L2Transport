package dev.xkmc.lasertransport.content.capability.wrapper;

import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.lasertransport.content.capability.base.ITargetTraceable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public record FakeCapabilityHolder<T>(Class<T> cls) implements ICapabilityHolder<T> {

	@Override
	public LazyOptional<T> getHolder(ITargetTraceable entity, BlockPos pos) {
		Level level = entity.getThis().getLevel();
		if (level != null) {
			BlockEntity target = level.getBlockEntity(pos);
			if (target instanceof IFakeCapabilityTile tile) {
				return tile.getCapability(this);
			}
			if (cls.isInstance(target)) {
				return LazyOptional.of(() -> Wrappers.cast(target));
			}
		}
		return LazyOptional.empty();
	}

	@Override
	public LazyOptional<T> getLeafHolder(BlockEntity target, Direction opposite) {
		return cls.isInstance(target) ? LazyOptional.of(() -> Wrappers.cast(target)) : LazyOptional.empty();
	}

	@Override
	public <C> boolean is(Capability<C> cap) {
		return false;
	}

	@Override
	public <C> boolean isSame(@NotNull ICapabilityHolder<C> cap) {
		return cap instanceof FakeCapabilityHolder holder && holder.cls() == cls();
	}

}
