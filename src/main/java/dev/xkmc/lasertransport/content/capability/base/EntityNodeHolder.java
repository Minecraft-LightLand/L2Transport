package dev.xkmc.lasertransport.content.capability.base;

import dev.xkmc.lasertransport.content.flow.INodeHolder;
import dev.xkmc.lasertransport.content.flow.NetworkType;
import dev.xkmc.lasertransport.content.flow.TransportContext;
import net.minecraft.core.BlockPos;

public interface EntityNodeHolder<T> extends INodeHolder<T> {

	INodeBlockEntity entity();

	@Override
	default BlockPos getIdentifier() {
		return entity().getThis().getBlockPos();
	}

	@Override
	default boolean isReady() {
		return entity().isReady();
	}

	@Override
	default NetworkType getNetworkType() {
		return entity().getConnector();
	}

	@Override
	default void refreshCoolDown(BlockPos target, boolean success, TransportContext<T> ctx) {
		entity().refreshCoolDown(target, success, ctx.simulate);
	}

}
