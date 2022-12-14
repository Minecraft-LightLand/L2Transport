package dev.xkmc.l2transport.content.capability.item;

import dev.xkmc.l2transport.content.flow.IContentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public record ItemHolder(ItemStack stack) implements IContentHolder<ItemStack> {

	@Override
	public int getCount() {
		return stack.getCount();
	}

	@Override
	public ItemStack get() {
		return stack;
	}

	@Override
	public ItemStack getCopy(int count) {
		if (count <= 0) return ItemStack.EMPTY;
		ItemStack copy = stack.copy();
		copy.setCount(count);
		return copy;
	}

	@Override
	public ItemStack empty() {
		return ItemStack.EMPTY;
	}

	@Override
	public MutableComponent getDesc() {
		return Component.empty().append(stack.getHoverName()).withStyle(stack.getRarity().getStyleModifier());
	}

}
