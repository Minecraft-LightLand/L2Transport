package dev.xkmc.l2transport.content.capability.fluid;

import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;
import dev.xkmc.l2transport.content.configurables.FluidConfigurable;

public interface IFluidNodeBlockEntity extends INodeBlockEntity {

	FluidConfigurable getConfig();

}
