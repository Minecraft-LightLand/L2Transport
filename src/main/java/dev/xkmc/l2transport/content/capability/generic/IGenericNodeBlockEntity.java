package dev.xkmc.l2transport.content.capability.generic;

import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;

public interface IGenericNodeBlockEntity extends INodeBlockEntity {

	boolean isContentValid(GenericHolder stack);

	int getMaxTransfer();

	ICapabilityEntry<?> getCapType();

	void setType(ICapabilityEntry<?> capType, long time);

	long getLastTypeTime();

}
