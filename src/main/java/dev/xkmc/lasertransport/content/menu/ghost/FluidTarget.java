package dev.xkmc.lasertransport.content.menu.ghost;

import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

public record FluidTarget(int x, int y, int w, int h, Consumer<FluidStack> con) {

}
