package dev.xkmc.l2transport.content.capability.generic;

import dev.xkmc.l2transport.content.capability.base.AbstractNodeTarget;
import dev.xkmc.l2transport.content.flow.IContentToken;
import dev.xkmc.l2transport.content.flow.RealToken;
import dev.xkmc.l2transport.init.L2Transport;
import net.minecraft.world.level.block.entity.BlockEntity;

public class GenericNodeTarget extends AbstractNodeTarget<GenericHolder> {

	private static int computeConsumption(HandlerWrapper handler, IContentToken<GenericHolder> token) {
		return handler.insert(token.get().get(), true);
	}

	private final HandlerWrapper handler;

	public GenericNodeTarget(BlockEntity be, HandlerWrapper handler, IContentToken<GenericHolder> token) {
		super(be, token, computeConsumption(handler, token));
		this.handler = handler;
	}

	@Override
	public void perform(RealToken<GenericHolder> real) {
		int drained = handler.insert(real.split(consumed), false);
		if (drained != consumed) {
			real.gain(consumed - drained);
			L2Transport.LOGGER.error("Mismatch behavior for content insertion at " + be);
		}
	}

}
