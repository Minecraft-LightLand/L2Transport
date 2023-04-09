package dev.xkmc.lasertransport.content.craft.tile;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lasertransport.util.Stack;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;


@SerialClass
public class ItemInventory implements IItemHandlerModifiable {

	@SerialClass.SerialField(toClient = true)
	public Stack stack = new Stack(ItemStack.EMPTY);

	private final IItemHolderNode be;

	public ItemInventory(IItemHolderNode be) {
		this.be = be;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		this.stack = new Stack(stack);
		be.markDirty();
	}

	@Override
	public int getSlots() {
		return 1;
	}

	@NotNull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return stack.stack();
	}

	@NotNull
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (!be.canInsert()) return stack;
		if (this.stack.stack().isEmpty()) {
			if (!simulate) {
				this.stack = new Stack(stack);
				be.markDirty();
			}
			return ItemStack.EMPTY;
		}
		return stack;
	}

	@NotNull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (!this.stack.stack().isEmpty()) {
			int count = this.stack.stack().getCount();
			int max = Math.min(Math.max(0, amount), count);
			if (max == 0) {
				return ItemStack.EMPTY;
			}
			ItemStack copy = this.stack.stack().copy();
			if (!simulate) {
				this.stack.stack().shrink(max);
				be.markDirty();
			}
			copy.setCount(max);
			return copy;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return slot == 0;
	}

}
