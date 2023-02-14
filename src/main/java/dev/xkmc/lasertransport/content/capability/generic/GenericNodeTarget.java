package dev.xkmc.lasertransport.content.capability.generic;

import dev.xkmc.lasertransport.content.capability.base.AbstractNodeTarget;
import dev.xkmc.lasertransport.content.flow.IContentToken;
import dev.xkmc.lasertransport.content.flow.RealToken;
import dev.xkmc.lasertransport.init.LaserTransport;
import net.minecraft.world.level.block.entity.BlockEntity;

public class GenericNodeTarget extends AbstractNodeTarget<GenericHolder> {

	private static long computeConsumption(HandlerWrapper handler, IContentToken<GenericHolder> token) {
		return handler.insert(token.get().get(), true);
	}

	private final HandlerWrapper handler;

	public GenericNodeTarget(BlockEntity be, HandlerWrapper handler, IContentToken<GenericHolder> token) {
		super(be, token, computeConsumption(handler, token));
		this.handler = handler;
	}

	@Override
	public void perform(RealToken<GenericHolder> real) {
		long drained = handler.insert(real.split(consumed), false);
		if (drained != consumed) {
			real.gain(consumed - drained);
			LaserTransport.LOGGER.error("Mismatch behavior for content insertion at " + be);
		}
	}

}
