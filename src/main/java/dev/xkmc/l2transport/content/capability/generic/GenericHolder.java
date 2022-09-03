package dev.xkmc.l2transport.content.capability.generic;

import dev.xkmc.l2transport.content.flow.IContentHolder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public record GenericHolder(ICapabilityEntry<?> type, ResourceLocation id,
							int amount) implements IContentHolder<GenericHolder> {

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
		return new GenericHolder(type, id, count);
	}

	@Override
	public GenericHolder empty() {
		return getCopy(0);
	}

	@Override
	public MutableComponent getDesc() {
		return type.getDesc(id, amount);
	}
}
