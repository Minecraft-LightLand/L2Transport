package dev.xkmc.l2transport.content.capability.base;

import dev.xkmc.l2transport.content.flow.IContentHolder;
import dev.xkmc.l2transport.content.flow.IContentToken;

public class GenericToken<T> implements IContentToken<T> {

	private final IContentHolder<T> holder;
	private long count;

	public GenericToken(IContentHolder<T> holder) {
		this.holder = holder;
		this.count = holder.getCount();
	}

	@Override
	public IContentHolder<T> get() {
		return holder;
	}

	@Override
	public long getAvailable() {
		return count;
	}

	@Override
	public void consume(long val) {
		count -= val;
	}

}
