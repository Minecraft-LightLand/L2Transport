package dev.xkmc.lasertransport.content.capability.generic;

import dev.xkmc.lasertransport.content.flow.IContentHolder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public record GenericHolder(ICapabilityEntry<?> type, ResourceLocation id,
							long amount) implements IContentHolder<GenericHolder> {

	public static final ResourceLocation EMPTY_ID = new ResourceLocation("empty");

	@Override
	public long getCount() {
		return amount;
	}

	@Override
	public GenericHolder get() {
		return this;
	}

	@Override
	public GenericHolder getCopy(long count) {
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
