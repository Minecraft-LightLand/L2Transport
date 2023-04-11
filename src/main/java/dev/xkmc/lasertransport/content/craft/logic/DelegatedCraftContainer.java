package dev.xkmc.lasertransport.content.craft.logic;

import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DelegatedCraftContainer extends CraftingContainer {

	private final CraftGrid grid;
	private final boolean isEmpty;

	public final int min;

	@SuppressWarnings("ConstantConditions")
	public DelegatedCraftContainer(CraftGrid grid) {
		super(null, 0, 0);
		this.grid = grid;
		boolean empty = true;
		int min = -1;
		for (var arr : grid.list()) {
			for (var ar : arr) {
				empty &= ar.stack().isEmpty();
				int count = ar.stack().getCount();
				min = count < 0 ? min : min < 0 ? count : Math.min(count, min);
			}
		}
		this.isEmpty = empty;
		this.min = min;
	}

	@Override
	public int getWidth() {
		return grid.width();
	}

	@Override
	public int getHeight() {
		return grid.height();
	}

	public int getContainerSize() {
		return getWidth() * grid.height();
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	@NotNull
	public ItemStack getItem(int slot) {
		return grid.list()[slot / getWidth()][slot % getWidth()].stack();
	}

	@NotNull
	public ItemStack removeItemNoUpdate(int slot) {
		return ItemStack.EMPTY;
	}

	@NotNull
	public ItemStack removeItem(int slot, int count) {
		return ItemStack.EMPTY;
	}

	public void setItem(int slot, @NotNull ItemStack stack) {
	}

	public void clearContent() {
	}

	public void fillStackedContents(@NotNull StackedContents helper) {
	}

}
