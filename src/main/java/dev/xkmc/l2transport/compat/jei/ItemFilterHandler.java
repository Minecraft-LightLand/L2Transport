package dev.xkmc.l2transport.compat.jei;

import dev.xkmc.l2library.util.code.Wrappers;
import dev.xkmc.l2transport.content.menu.ItemConfigScreen;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ItemFilterHandler implements IGhostIngredientHandler<ItemConfigScreen> {

	@Override
	public <I> List<Target<I>> getTargets(ItemConfigScreen gui, I ingredient, boolean doStart) {
		return ingredient instanceof ItemStack ? Wrappers.cast(gui.getTargets().stream().map(RTarget::new).toList()) : List.of();
	}

	@Override
	public void onComplete() {

	}

	public record RTarget(ItemConfigScreen.Target target) implements Target<ItemStack> {

		@Override
		public Rect2i getArea() {
			return new Rect2i(target.x(), target.y(), target.w(), target.h());
		}

		@Override
		public void accept(ItemStack ingredient) {
			target.con().accept(ingredient);
		}
	}

}
