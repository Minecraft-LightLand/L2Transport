package dev.xkmc.lasertransport.content.capability.generic;

import dev.xkmc.lasertransport.content.capability.base.ITargetTraceable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;

import java.util.Optional;

public interface ICapabilityHolder<T> {

	Optional<T> getHolder(ITargetTraceable entity, BlockPos pos);

	Optional<T> getLeafHolder(BlockEntity target, Direction opposite);

	<C> boolean is(Capability<C> cap);
}
