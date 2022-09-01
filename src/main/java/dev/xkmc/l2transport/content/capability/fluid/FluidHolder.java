package dev.xkmc.l2transport.content.capability.fluid;

import dev.xkmc.l2transport.content.flow.IContentHolder;
import net.minecraftforge.fluids.FluidStack;

public record FluidHolder(FluidStack stack) implements IContentHolder<FluidStack> {

	@Override
	public int getCount() {
		return stack.getAmount();
	}

	@Override
	public FluidStack get() {
		return stack;
	}

	@Override
	public FluidStack getCopy(int count) {
		if (count <= 0) return FluidStack.EMPTY;
		FluidStack copy = stack.copy();
		copy.setAmount(count);
		return copy;
	}

	@Override
	public FluidStack empty() {
		return FluidStack.EMPTY;
	}

}
