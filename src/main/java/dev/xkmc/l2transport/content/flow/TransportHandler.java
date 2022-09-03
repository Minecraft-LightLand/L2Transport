package dev.xkmc.l2transport.content.flow;

import dev.xkmc.l2transport.content.capability.base.GenericToken;

public class TransportHandler {

	public static <T> INetworkNode<T> broadcastRecursive(TransportContext<T> ctx, INodeHolder<T> node, IContentToken<T> token) {
		if (!node.isValid(token.get())) {
			return BroadcastTree.empty(node, token);
		}
		TreeBuilder<T> root = BroadcastTree.constructRoot(node, token);
		root.iterate(ctx, node.getTargets());
		return root.build();
	}

	public static <T> int insert(INodeHolder<T> node, IContentHolder<T> holder, boolean simulate) {
		TransportContext<T> ctx = new TransportContext<>(simulate);
		IContentToken<T> token = new GenericToken<>(holder);
		if (!node.isReady() || !node.isValid(holder)) {
			return 0;
		}
		INetworkNode<T> tree = TransportHandler.broadcastRecursive(ctx, node, token);
		tree.refreshCoolDown(ctx, tree.hasAction());
		if (!tree.hasAction()) {
			return 0;
		}
		if (!simulate) {
			RealToken<T> real = holder.toReal();
			tree.perform(real);
			return real.getRemain();
		} else return tree.getConsumed();
	}

}
