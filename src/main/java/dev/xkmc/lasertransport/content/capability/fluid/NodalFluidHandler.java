package dev.xkmc.lasertransport.content.capability.fluid;

import dev.xkmc.lasertransport.content.capability.base.FluidStackNode;
import dev.xkmc.lasertransport.content.flow.IContentHolder;
import dev.xkmc.lasertransport.content.flow.TransportHandler;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public record NodalFluidHandler(IFluidNodeBlockEntity entity) implements IFluidHandler, FluidStackNode {

	@Override
	public int getTanks() {
		return 1;
	}

	@Override
	public @NotNull FluidStack getFluidInTank(int tank) {
		return FluidStack.EMPTY;
	}

	@Override
	public int getTankCapacity(int tank) {
		return (int) entity.getConfig().getMaxTransfer();
	}

	@Override
	public boolean isFluidValid(int slot, @NotNull FluidStack stack) {
		return entity.getConfig().isStackValid(stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		long max = Math.min(resource.getAmount(), entity.getConfig().getMaxTransfer());
		FluidStack copy = resource.copy();
		copy.setAmount((int) max);
		return (int) TransportHandler.insert(this, new FluidHolder(copy), action.simulate());
	}

	@Override
	public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
		return FluidStack.EMPTY;
	}

	@Override
	public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
		return FluidStack.EMPTY;
	}

	@Override
	public boolean isValid(IContentHolder<FluidStack> token) {
		return entity.getConfig().isStackValid(token.get());
	}

}
