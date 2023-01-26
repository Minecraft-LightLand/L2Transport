package dev.xkmc.l2transport.content.capability.item;

import dev.xkmc.l2transport.content.capability.base.ItemStackNode;
import dev.xkmc.l2transport.content.flow.IContentHolder;
import dev.xkmc.l2transport.content.flow.TransportHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public record NodalItemHandler(IItemNodeBlockEntity entity) implements IItemHandler, ItemStackNode {

	@Override
	public int getSlots() {
		return 1;
	}

	@Override
	public @NotNull ItemStack getStackInSlot(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
		long consumed = TransportHandler.insert(this, new ItemHolder(stack), simulate);
		if (consumed == 0) {
			return stack;
		}
		if (consumed == stack.getCount()) {
			return ItemStack.EMPTY;
		}
		ItemStack ans = stack.copy();
		ans.shrink((int) consumed);
		return ans;
	}

	@Override
	public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}

	@Override
	public boolean isItemValid(int slot, @NotNull ItemStack stack) {
		return entity.isItemStackValid(stack);
	}

	@Override
	public boolean isValid(IContentHolder<ItemStack> token) {
		return entity.isItemStackValid(token.get());
	}

}
