package dev.xkmc.l2transport.content.capability.base;

import dev.xkmc.l2transport.content.capability.fluid.FluidNodeTarget;
import dev.xkmc.l2transport.content.flow.IContentToken;
import dev.xkmc.l2transport.content.flow.INetworkNode;
import dev.xkmc.l2transport.content.flow.INodeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public interface FluidStackNode extends BaseNodeHolder<FluidStack, IFluidHandler> {

	@Override
	default Capability<IFluidHandler> getCapability() {
		return ForgeCapabilities.FLUID_HANDLER;
	}

	@Nullable
	@Override
	default INodeHolder<FluidStack> castNode(IFluidHandler cap) {
		return (cap instanceof FluidStackNode node) ? node : null;
	}

	@Override
	default INetworkNode<FluidStack> createLeaf(BlockEntity target, IFluidHandler cap, IContentToken<FluidStack> token) {
		return new FluidNodeTarget(target, cap, token);
	}

}
