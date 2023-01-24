package dev.xkmc.l2transport.content.flow;

import net.minecraft.core.BlockPos;

public record ErrorNode<T>(BlockPos pos) implements INetworkNode<T> {

	@Override
	public long getConsumed() {
		return 0;
	}

	@Override
	public void refreshCoolDown(TransportContext<T> ctx, boolean success) {

	}

	@Override
	public void perform(RealToken<T> token) {

	}

	@Override
	public boolean hasAction() {
		return false;
	}

	@Override
	public BlockPos getIdentifier() {
		return pos;
	}

}
