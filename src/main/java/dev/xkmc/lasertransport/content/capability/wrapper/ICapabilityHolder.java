package dev.xkmc.lasertransport.content.capability.wrapper;

import dev.xkmc.lasertransport.content.capability.base.ITargetTraceable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public interface ICapabilityHolder<T> {

	LazyOptional<T> getHolder(ITargetTraceable entity, BlockPos pos);

	LazyOptional<T> getLeafHolder(BlockEntity target, Direction opposite);

	<C> boolean is(Capability<C> cap);

	<C> boolean isSame(@NotNull ICapabilityHolder<C> cap);
}
