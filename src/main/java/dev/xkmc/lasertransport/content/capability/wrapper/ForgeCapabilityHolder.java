package dev.xkmc.lasertransport.content.capability.wrapper;

import dev.xkmc.lasertransport.content.capability.base.ITargetTraceable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public record ForgeCapabilityHolder<T>(Capability<T> cap) implements ICapabilityHolder<T> {

	@Override
	public LazyOptional<T> getHolder(ITargetTraceable entity, BlockPos pos) {
		return entity.getCapability(cap, pos);
	}

	@Override
	public LazyOptional<T> getLeafHolder(BlockEntity target, Direction face) {
		return target.getCapability(cap, face);
	}

	@Override
	public <C> boolean is(Capability<C> cap) {
		return this.cap == cap;
	}

	@Override
	public <C> boolean isSame(@NotNull ICapabilityHolder<C> cap) {
		return cap instanceof ForgeCapabilityHolder holder && holder.cap() == this.cap();
	}

}
