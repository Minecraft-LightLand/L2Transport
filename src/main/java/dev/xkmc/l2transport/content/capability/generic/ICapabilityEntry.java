package dev.xkmc.l2transport.content.capability.generic;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

public interface ICapabilityEntry<T> {

	ResourceLocation id();

	Capability<T> cap();

	HandlerWrapper parse(T t);

	MutableComponent getDesc(ResourceLocation id, int amount);

	int getDefaultMax();

	GenericHolder empty();

	T parseHandler(NodalGenericHandler handler);
}
