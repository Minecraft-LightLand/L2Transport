package dev.xkmc.l2transport.content.capability.fluid;

import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;
import net.minecraftforge.fluids.FluidStack;

public interface IFluidNodeBlockEntity extends INodeBlockEntity {

	boolean isFluidStackValid(FluidStack stack);

	int getMaxTransfer();

}
