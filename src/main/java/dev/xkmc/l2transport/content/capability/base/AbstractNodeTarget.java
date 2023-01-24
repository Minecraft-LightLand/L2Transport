package dev.xkmc.l2transport.content.capability.base;

import dev.xkmc.l2transport.content.flow.IContentToken;
import dev.xkmc.l2transport.content.flow.INetworkNode;
import dev.xkmc.l2transport.content.flow.TransportContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractNodeTarget<T> implements INetworkNode<T> {

	protected final BlockEntity be;
	protected final IContentToken<T> token;
	protected final long consumed;

	public AbstractNodeTarget(BlockEntity be, IContentToken<T> token, int consumed) {
		this.be = be;
		this.token = token;
		this.consumed = consumed;
		token.consume(consumed);
	}

	@Override
	public long getConsumed() {
		return consumed;
	}

	@Override
	public void refreshCoolDown(TransportContext<T> ctx, boolean success) {
	}

	@Override
	public boolean hasAction() {
		return consumed > 0;
	}

	@Override
	public BlockPos getIdentifier() {
		return be.getBlockPos();
	}

}
