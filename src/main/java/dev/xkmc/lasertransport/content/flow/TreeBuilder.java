package dev.xkmc.lasertransport.content.flow;

import java.util.ArrayList;
import java.util.List;

class TreeBuilder<T> implements IContentToken<T> {

	public final NetworkType type;

	private final INodeHolder<T> node;
	private final IContentToken<T> token;
	private final List<INetworkNode<T>> children = new ArrayList<>();

	private long consumed = 0, size;
	private boolean valid = true;

	TreeBuilder(INodeHolder<T> node, IContentToken<T> token) {
		this.type = node.getNetworkType();
		this.node = node;
		this.token = token;
	}

	public void iterate(TransportContext<T> ctx, List<INodeSupplier<T>> targets) {
		size = targets.size();
		for (INodeSupplier<T> pos : targets) {
			append(ctx, pos);
			if (!type.shouldContinue(token.getAvailable(), consumed, size)) break;
		}
	}

	public void append(TransportContext<T> ctx, INodeSupplier<T> factory) {
		INetworkNode<T> node;
		long avail = getAvailable();
		if (factory.isValid() && ctx.add(factory.getIdentifier())) {
			node = factory.constructNode(ctx, this);
		} else {
			node = new ErrorNode<>(factory.getIdentifier());
		}
		long c = node.getConsumed();
		valid &= type.testConsumption(avail, c);
		children.add(node);
	}

	public INetworkNode<T> build() {
		long val = 0;
		if (valid && token.getAvailable() >= consumed) {
			token.consume(consumed);
			val = consumed;
		}
		return new BroadcastTree<>(node, children, token.get(), val);
	}

	@Override
	public long getAvailable() {
		return type.provide(token.getAvailable(), consumed, size);
	}

	@Override
	public IContentHolder<T> get() {
		return token.get();
	}

	@Override
	public void consume(long count) {
		consumed += count;
	}

}
