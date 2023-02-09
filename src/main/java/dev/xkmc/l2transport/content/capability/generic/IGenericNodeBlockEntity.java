package dev.xkmc.l2transport.content.capability.generic;

import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;
import dev.xkmc.l2transport.content.configurables.FluxConfigurable;

public interface IGenericNodeBlockEntity extends INodeBlockEntity {

	ICapabilityEntry<?> getCapType();

	void setType(ICapabilityEntry<?> capType, long time);

	long getLastTypeTime();

	FluxConfigurable getConfig();

}
