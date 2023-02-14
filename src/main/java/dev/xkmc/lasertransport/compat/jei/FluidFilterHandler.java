package dev.xkmc.lasertransport.compat.jei;

import dev.xkmc.l2library.util.code.Wrappers;
import dev.xkmc.lasertransport.content.menu.filter.FluidConfigScreen;
import dev.xkmc.lasertransport.content.menu.ghost.FluidTarget;
import dev.xkmc.lasertransport.content.menu.ghost.ItemTarget;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class FluidFilterHandler implements IGhostIngredientHandler<FluidConfigScreen> {

	@Override
	public <I> List<Target<I>> getTargets(FluidConfigScreen gui, I ingredient, boolean doStart) {
		if (ingredient instanceof ItemStack) {
			return Wrappers.cast(gui.getTargets().stream().map(RITarget::new).toList());
		}
		if (ingredient instanceof FluidStack) {
			return Wrappers.cast(gui.getFluidTargets().stream().map(RFTarget::new).toList());
		}
		return List.of();
	}

	@Override
	public void onComplete() {

	}

	public record RITarget(ItemTarget target) implements Target<ItemStack> {

		@Override
		public Rect2i getArea() {
			return new Rect2i(target.x(), target.y(), target.w(), target.h());
		}

		@Override
		public void accept(ItemStack ingredient) {
			target.con().accept(ingredient);
		}
	}

	public record RFTarget(FluidTarget target) implements Target<FluidStack> {

		@Override
		public Rect2i getArea() {
			return new Rect2i(target.x(), target.y(), target.w(), target.h());
		}

		@Override
		public void accept(FluidStack ingredient) {
			target.con().accept(ingredient);
		}
	}

}
