package dev.xkmc.l2transport.content.capability.fluid;

import dev.xkmc.l2transport.content.flow.IContentToken;
import dev.xkmc.l2transport.content.flow.INetworkNode;
import dev.xkmc.l2transport.content.flow.RealToken;
import dev.xkmc.l2transport.content.flow.TransportContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

public final class CauldronTarget implements INetworkNode<FluidStack> {

	private final Level level;
	private final BlockPos pos;
	private final FluidType type;
	private final boolean canUse;

	public CauldronTarget(Level level, BlockPos pos, IContentToken<FluidStack> stack) {
		this.level = level;
		this.pos = pos;
		type = stack.get().get().getFluid().getFluidType();
		canUse = (type == ForgeMod.WATER_TYPE.get() || type == ForgeMod.LAVA_TYPE.get()) &&
				stack.getAvailable() >= FluidType.BUCKET_VOLUME &&
				level.getBlockState(pos) == Blocks.CAULDRON.defaultBlockState();
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
		if (level.getBlockState(pos) != Blocks.CAULDRON.defaultBlockState()) {
			return;
		}
		token.split(FluidType.BUCKET_VOLUME);
		BlockState state;
		if (type == ForgeMod.LAVA_TYPE.get()) {
			state = Blocks.LAVA_CAULDRON.defaultBlockState();
		} else {
			state = Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3);
		}
		level.setBlockAndUpdate(pos, state);
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
