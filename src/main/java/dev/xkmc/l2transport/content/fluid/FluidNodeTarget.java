package dev.xkmc.l2transport.content.fluid;

import dev.xkmc.l2transport.content.api.IContentToken;
import dev.xkmc.l2transport.content.api.RealToken;
import dev.xkmc.l2transport.content.core.AbstractNodeTarget;
import dev.xkmc.l2transport.init.L2Transport;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidNodeTarget extends AbstractNodeTarget<FluidStack> {

	private static int computeConsumption(IFluidHandler handler, IContentToken<FluidStack> token) {
		return handler.fill(token.get().get(), IFluidHandler.FluidAction.SIMULATE);
	}

	private final IFluidHandler handler;

	public FluidNodeTarget(BlockEntity be, IFluidHandler handler, IContentToken<FluidStack> token) {
		super(be, token, computeConsumption(handler, token));
		this.handler = handler;
	}

	@Override
	public void perform(RealToken<FluidStack> real) {
		int drained = handler.fill(real.split(consumed), IFluidHandler.FluidAction.EXECUTE);
		if (drained != consumed) {
			real.gain(consumed - drained);
			L2Transport.LOGGER.error("Mismatch behavior for fluid insertion at " + be);
		}
	}

}
