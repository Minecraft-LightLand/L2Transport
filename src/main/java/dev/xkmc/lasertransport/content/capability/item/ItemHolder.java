package dev.xkmc.lasertransport.content.capability.item;

import dev.xkmc.lasertransport.content.flow.IContentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public record ItemHolder(ItemStack stack) implements IContentHolder<ItemStack> {

	@Override
	public long getCount() {
		return stack.getCount();
	}

	@Override
	public ItemStack get() {
		return stack;
	}

	@Override
	public ItemStack getCopy(long count) {
		if (count <= 0) return ItemStack.EMPTY;
		ItemStack copy = stack.copy();
		copy.setCount((int) count);
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
