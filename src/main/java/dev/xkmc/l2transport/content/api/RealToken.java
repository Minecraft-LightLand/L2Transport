package dev.xkmc.l2transport.content.api;

import dev.xkmc.l2transport.init.L2Transport;

/**
 * Used to guard item usage, to make sure that no item dupe happens
 */
public class RealToken<T> {

	private final IContentHolder<T> holder;
	private int count;

	public RealToken(IContentHolder<T> holder, int count) {
		this.holder = holder;
		this.count = count;
	}

	public T split(int consumed) {
		if (consumed > count) {
			L2Transport.LOGGER.error("Consumes more than available. Consumed: " + consumed + ", Available: " + count + ", Content: " + holder.get());
		}
		int val = Math.min(consumed, count);
		count -= val;
		return holder.getCopy(val);
	}

	public void gain(int remain) {
		count += remain;
	}

	public int getRemain() {
		return count;
	}

}
