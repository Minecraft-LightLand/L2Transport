package dev.xkmc.l2transport.content.menu.container;

import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class DummyFluidContainer extends SimpleContainer implements FluidContainer {

	private final NonNullList<FluidStack> list;

	public DummyFluidContainer(int size) {
		super(size);
		list = NonNullList.withSize(size, FluidStack.EMPTY);
	}

	@Override
	public FluidStack getFluid(int slot) {
		return list.get(slot);
	}

	@Override
	public ItemStack getItem(int slot) {
		return getItemImpl(slot);
	}

}
