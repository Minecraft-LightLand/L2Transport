package dev.xkmc.l2transport.content.tile.base;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IRenderableFluidNode extends IRenderableNode {

	FluidStack getFluid();

}
