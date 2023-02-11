package dev.xkmc.l2transport.content.configurables;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ReadOnlyContainer extends Container {

	@Override
	default ItemStack removeItem(int ind, int amount) {
		return ItemStack.EMPTY;
	}

	@Override
	default ItemStack removeItemNoUpdate(int ind) {
		return ItemStack.EMPTY;
	}

	@Override
	default void setItem(int ind, ItemStack stack) {
	}

	@Override
	default void setChanged() {
	}

	@Override
	default boolean stillValid(Player player) {
		return player.isAlive();
	}

	@Override
	default void clearContent() {

	}

}
