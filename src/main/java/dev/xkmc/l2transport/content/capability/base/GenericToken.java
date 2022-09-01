package dev.xkmc.l2transport.content.capability.base;

import dev.xkmc.l2transport.content.flow.IContentHolder;
import dev.xkmc.l2transport.content.flow.IContentToken;

public class GenericToken<T> implements IContentToken<T> {

	private final IContentHolder<T> holder;
	private int count;

	public GenericToken(IContentHolder<T> holder) {
		this.holder = holder;
		this.count = holder.getCount();
	}

	@Override
	public IContentHolder<T> get() {
		return holder;
	}

	@Override
	public int getAvailable() {
		return count;
	}

	@Override
	public void consume(int val) {
		count -= val;
	}

}
