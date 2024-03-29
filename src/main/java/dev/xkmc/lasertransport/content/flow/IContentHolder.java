package dev.xkmc.lasertransport.content.flow;

import net.minecraft.network.chat.MutableComponent;

/**
 * Holder of content, such as ItemStack, FluidStack, Energy, Gas.
 * This should entity immutable.
 */
public interface IContentHolder<T> {

	/**
	 * returns the original amount.
	 */
	long getCount();

	/**
	 * returns the original stack. Should not entity modified
	 */
	T get();

	/**
	 * returns a copy of the original stack.
	 */
	T getCopy(long count);

	/**
	 * returns the empty representation of the content type
	 */
	T empty();

	/**
	 * returns a guarded stack
	 */
	default RealToken<T> toReal() {
		return new RealToken<>(this, getCount());
	}

	MutableComponent getDesc();

}
