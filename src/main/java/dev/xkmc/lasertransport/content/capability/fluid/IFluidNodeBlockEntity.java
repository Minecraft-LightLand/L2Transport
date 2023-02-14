package dev.xkmc.lasertransport.content.capability.fluid;

import dev.xkmc.lasertransport.content.capability.base.INodeBlockEntity;
import dev.xkmc.lasertransport.content.configurables.FluidConfigurable;

public interface IFluidNodeBlockEntity extends INodeBlockEntity {

	FluidConfigurable getConfig();

}
