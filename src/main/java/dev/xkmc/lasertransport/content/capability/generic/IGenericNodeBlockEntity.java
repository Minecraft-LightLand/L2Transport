package dev.xkmc.lasertransport.content.capability.generic;

import dev.xkmc.lasertransport.content.capability.base.INodeBlockEntity;
import dev.xkmc.lasertransport.content.configurables.FluxConfigurable;

public interface IGenericNodeBlockEntity extends INodeBlockEntity {

	ICapabilityEntry<?> getCapType();

	void setType(ICapabilityEntry<?> capType, long time);

	long getLastTypeTime();

	FluxConfigurable getConfig();

}
