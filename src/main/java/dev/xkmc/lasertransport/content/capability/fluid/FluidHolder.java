package dev.xkmc.lasertransport.content.capability.fluid;

import dev.xkmc.lasertransport.content.flow.IContentHolder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.fluids.FluidStack;

public record FluidHolder(FluidStack stack) implements IContentHolder<FluidStack> {

	@Override
	public long getCount() {
		return stack.getAmount();
	}

	@Override
	public FluidStack get() {
		return stack;
	}

	@Override
	public FluidStack getCopy(long count) {
		if (count <= 0) return FluidStack.EMPTY;
		FluidStack copy = stack.copy();
		copy.setAmount((int) count);
		return copy;
	}

	@Override
	public FluidStack empty() {
		return FluidStack.EMPTY;
	}

	@Override
	public MutableComponent getDesc() {
		return stack.getDisplayName().copy().withStyle(stack.getFluid().getFluidType().getRarity().getStyleModifier());
	}

}
