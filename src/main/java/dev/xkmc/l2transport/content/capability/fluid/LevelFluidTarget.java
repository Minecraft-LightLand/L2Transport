package dev.xkmc.l2transport.content.capability.fluid;

import dev.xkmc.l2transport.content.flow.IContentToken;
import dev.xkmc.l2transport.content.flow.INetworkNode;
import dev.xkmc.l2transport.content.flow.RealToken;
import dev.xkmc.l2transport.content.flow.TransportContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

public final class LevelFluidTarget implements INetworkNode<FluidStack> {

	private final Level level;
	private final BlockPos pos;
	private final FluidStack stack;
	private final Fluid type;
	private final boolean canUse;

	public LevelFluidTarget(Level level, BlockPos pos, IContentToken<FluidStack> stack) {
		this.level = level;
		this.pos = pos;
		this.stack = stack.get().get();
		type = this.stack.getFluid();
		canUse = type.getFluidType().canBePlacedInLevel(level, pos, stack.get().get()) &&
				stack.getAvailable() >= FluidType.BUCKET_VOLUME &&
				level.getBlockState(pos).canBeReplaced(type);
	}

	@Override
	public long getConsumed() {
		return canUse ? FluidType.BUCKET_VOLUME : 0;
	}

	@Override
	public void refreshCoolDown(TransportContext<FluidStack> ctx, boolean success) {

	}

	@Override
	public void perform(RealToken<FluidStack> token) {
		if (!level.getBlockState(pos).canBeReplaced(type)) {
			return;
		}
		token.split(FluidType.BUCKET_VOLUME);
		level.setBlockAndUpdate(pos, type.getFluidType().getBlockForFluidState(level, pos, type.defaultFluidState()));
	}

	@Override
	public boolean hasAction() {
		return canUse;
	}

	@Override
	public BlockPos getIdentifier() {
		return pos;
	}

}
