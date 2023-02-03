package dev.xkmc.l2transport.content.flow;

import dev.xkmc.l2transport.init.L2Transport;

/**
 * Used to guard item usage, to make sure that no item dupe happens
 */
public class RealToken<T> {

	private final IContentHolder<T> holder;
	private final long original;
	private long count;

	public RealToken(IContentHolder<T> holder, long count) {
		this.holder = holder;
		this.original = count;
		this.count = count;
	}

	public T split(long consumed) {
		if (consumed > count) {
			L2Transport.LOGGER.error("Consumes more than available. Consumed: " + consumed + ", Available: " + count + ", Content: " + holder.get());
		}
		long val = Math.min(consumed, count);
		count -= val;
		return holder.getCopy(val);
	}

	public void gain(long remain) {
		count += remain;
	}

	public long getConsumed() {
		return original - count;
	}

}
