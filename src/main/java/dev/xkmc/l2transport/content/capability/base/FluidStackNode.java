package dev.xkmc.l2transport.content.capability.base;

import dev.xkmc.l2transport.content.capability.fluid.CauldronTarget;
import dev.xkmc.l2transport.content.capability.fluid.FluidNodeTarget;
import dev.xkmc.l2transport.content.capability.fluid.LevelFluidTarget;
import dev.xkmc.l2transport.content.flow.ErrorNode;
import dev.xkmc.l2transport.content.flow.IContentToken;
import dev.xkmc.l2transport.content.flow.INetworkNode;
import dev.xkmc.l2transport.content.flow.INodeHolder;
import dev.xkmc.l2transport.content.upgrades.UpgradeFlag;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
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

	default INetworkNode<FluidStack> getWorldNode(BlockPos pos, IContentToken<FluidStack> token) {
		Level level = entity().getThis().getLevel();
		assert level != null;
		if (level.getBlockState(pos).is(BlockTags.CAULDRONS)) {
			return new CauldronTarget(level, pos, token);
		}
		if (entity().getUpgrade(UpgradeFlag.LEVEL) != null) {
			return new LevelFluidTarget(level, pos, token);
		}
		return new ErrorNode<>(pos);
	}

}
