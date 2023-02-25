package dev.xkmc.lasertransport.content.capability.generic;

import dev.xkmc.lasertransport.content.capability.base.ITargetTraceable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;

import java.util.Optional;

public record ForgeCapabilityHolder<T>(Capability<T> cap) implements ICapabilityHolder<T> {

	@Override
	public Optional<T> getHolder(ITargetTraceable entity, BlockPos pos) {
		return entity.getCapability(cap, pos).resolve();
	}

	@Override
	public Optional<T> getLeafHolder(BlockEntity target, Direction face) {
		return target.getCapability(cap, face).resolve();
	}

	@Override
	public <C> boolean is(Capability<C> cap) {
		return this.cap == cap;
	}

}
