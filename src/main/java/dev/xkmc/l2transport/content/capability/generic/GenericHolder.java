package dev.xkmc.l2transport.content.capability.generic;

import dev.xkmc.l2transport.content.flow.IContentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public record GenericHolder(ResourceLocation id, int amount) implements IContentHolder<GenericHolder> {

	private static final GenericHolder EMPTY = new GenericHolder(new ResourceLocation("empty"), 0);

	@Override
	public int getCount() {
		return amount;
	}

	@Override
	public GenericHolder get() {
		return this;
	}

	@Override
	public GenericHolder getCopy(int count) {
		return new GenericHolder(id, count);
	}

	@Override
	public GenericHolder empty() {
		return EMPTY;
	}

	@Override
	public MutableComponent getDesc() {
		return Component.literal(amount + " " + id.toString());
	}
}
