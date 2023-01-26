package dev.xkmc.l2transport.content.capability.fluid;

import dev.xkmc.l2transport.content.capability.base.FluidStackNode;
import dev.xkmc.l2transport.content.flow.*;
import net.minecraft.core.BlockPos;
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
		return entity.getMaxTransfer();
	}

	@Override
	public boolean isFluidValid(int slot, @NotNull FluidStack stack) {
		return entity.isFluidStackValid(stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		return (int) TransportHandler.insert(this, new FluidHolder(resource), action.simulate());
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
		return entity.isFluidStackValid(token.get());
	}

}
