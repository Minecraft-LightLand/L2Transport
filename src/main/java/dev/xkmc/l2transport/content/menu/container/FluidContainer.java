package dev.xkmc.l2transport.content.menu.container;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface FluidContainer extends Container {

	FluidStack getFluid(int slot);

	default ItemStack getItemImpl(int slot) {
		FluidStack fluid = getFluid(slot);
		if (fluid.isEmpty() || fluid.hasTag()) {
			return ItemStack.EMPTY;
		}
		return fluid.getFluid().getBucket().getDefaultInstance();
	}

}
