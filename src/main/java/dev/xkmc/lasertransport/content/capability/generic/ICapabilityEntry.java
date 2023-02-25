package dev.xkmc.lasertransport.content.capability.generic;

import dev.xkmc.lasertransport.content.configurables.NumericConfigurator;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface ICapabilityEntry<T> {

	ResourceLocation id();

	ICapabilityHolder<T> cap();

	HandlerWrapper parse(T t);

	MutableComponent getKindDesc(ResourceLocation id);

	MutableComponent getDesc(ResourceLocation id, long amount);

	int getDefaultMax();

	GenericHolder empty();

	/**
	 * be sure to return an instance of INodeHandlerWrapper
	 */
	T parseHandler(NodalGenericHandler handler);

	MutableComponent getTypeDesc();

	ItemStack getIcon();

	NumericConfigurator getScale();

}
