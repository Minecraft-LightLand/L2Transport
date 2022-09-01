package dev.xkmc.l2transport.content.capability.base;

import dev.xkmc.l2transport.content.flow.IContentToken;
import dev.xkmc.l2transport.content.flow.INetworkNode;
import dev.xkmc.l2transport.content.flow.INodeSupplier;
import dev.xkmc.l2transport.content.flow.TransportContext;
import net.minecraft.core.BlockPos;

import java.util.function.BiFunction;

public record SimpleNodeSupplier<T>(BlockPos pos, boolean isValid,
									BiFunction<TransportContext<T>, IContentToken<T>, INetworkNode<T>> factory) implements INodeSupplier<T> {

	@Override
	public INetworkNode<T> constructNode(TransportContext<T> ctx, IContentToken<T> token) {
		return factory.apply(ctx, token);
	}

	@Override
	public BlockPos getIdentifier() {
		return pos;
	}
}
