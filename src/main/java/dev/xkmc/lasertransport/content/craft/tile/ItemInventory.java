package dev.xkmc.lasertransport.content.craft.tile;

import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


@SerialClass
public class ItemInventory implements IItemHandlerModifiable {

	public record LargeStack(ItemStack stack, int count) {

		public static LargeStack of(ItemStack stack) {
			ItemStack copy = stack.copy();
			copy.setCount(1);
			return new LargeStack(stack, stack.getCount());
		}

		public ItemStack toStack() {
			ItemStack ans = stack.copy();
			ans.setCount(count);
			return ans;
		}
	}

	@SerialClass.SerialField(toClient = true)
	public ArrayList<LargeStack> list = new ArrayList<>();

	private final IItemHolderNode be;

	public ItemInventory(IItemHolderNode be) {
		this.be = be;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		list.clear();
		if (!stack.isEmpty())
			list.add(LargeStack.of(stack));
		be.markDirty();
	}

	public void forceAdd(ItemStack stack) {
		if (!stack.isEmpty())
			list.add(LargeStack.of(stack));
		be.markDirty();
	}

	@Override
	public int getSlots() {
		return 1;
	}

	public List<ItemStack> getAll() {
		return list.stream().map(LargeStack::toStack).toList();
	}

	@NotNull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return slot < 0 || slot >= list.size() ? ItemStack.EMPTY : list.get(slot).toStack();
	}

	@NotNull
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (!be.canInsert()) return stack;
		if (list.isEmpty()) {
			if (!simulate) {
				if (!stack.isEmpty())
					list.add(LargeStack.of(stack));
				be.markDirty();
			}
			return ItemStack.EMPTY;
		}
		return stack;
	}

	@NotNull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (!list.isEmpty()) {
			int count = list.get(0).count();
			int max = Math.min(Math.max(0, amount), count);
			if (max == 0) {
				return ItemStack.EMPTY;
			}
			ItemStack copy = this.list.get(0).toStack();
			if (!simulate) {
				copy.shrink(max);
				if (copy.isEmpty()) {
					list.remove(0);
				}
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
