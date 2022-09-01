package dev.xkmc.l2transport.content.core;

import dev.xkmc.l2transport.content.api.IContentToken;
import dev.xkmc.l2transport.content.api.INetworkNode;
import dev.xkmc.l2transport.content.api.INodeSupplier;
import dev.xkmc.l2transport.content.api.TransportContext;
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
