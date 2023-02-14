package dev.xkmc.lasertransport.content.capability.fluid;

import dev.xkmc.lasertransport.content.capability.base.AbstractNodeTarget;
import dev.xkmc.lasertransport.content.flow.IContentToken;
import dev.xkmc.lasertransport.content.flow.RealToken;
import dev.xkmc.lasertransport.init.LaserTransport;
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
		long drained = handler.fill(real.split(consumed), IFluidHandler.FluidAction.EXECUTE);
		if (drained != consumed) {
			real.gain(consumed - drained);
			LaserTransport.LOGGER.error("Mismatch behavior for fluid insertion at " + be);
		}
	}

}
