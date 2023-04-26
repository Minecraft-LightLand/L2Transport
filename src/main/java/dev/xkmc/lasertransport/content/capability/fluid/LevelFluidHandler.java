package dev.xkmc.lasertransport.content.capability.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public record LevelFluidHandler(Level level, BlockPos pos, BlockState state) implements IFluidHandler {

	public static FluidStack getFluid(Level level, BlockPos pos, BlockState state) {
		FluidState fluidState = state.getFluidState();
		if (fluidState.isEmpty() || !fluidState.isSource()) return FluidStack.EMPTY;
		BlockState correct = fluidState.getFluidType().getBlockForFluidState(level, pos, fluidState.getType().defaultFluidState());
		if (state != correct) return FluidStack.EMPTY;
		return new FluidStack(fluidState.getType(), 1000);
	}

	@Override
	public int getTanks() {
		return 1;
	}

	@NotNull
	@Override
	public FluidStack getFluidInTank(int tank) {
		return getFluid(level, pos, state);
	}

	@Override
	public int getTankCapacity(int tank) {
		return FluidType.BUCKET_VOLUME;
	}

	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
		return stack.getFluid().getFluidType().canBePlacedInLevel(level, pos, stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if (!state.canBeReplaced(resource.getFluid())) return 0;
		if (level.getFluidState(pos).getType() == resource.getFluid()) return 0;
		if (resource.getAmount() < 1000) return 0;
		if (action.execute()) {
			BlockState correct = resource.getFluid().getFluidType()
					.getBlockForFluidState(level, pos, resource.getFluid().defaultFluidState());
			level.setBlockAndUpdate(pos, state);
		}
		return 1000;
	}

	@Override
	public @NotNull
	FluidStack drain(FluidStack resource, FluidAction action) {
		if (resource.getAmount() < FluidType.BUCKET_VOLUME) {
			return FluidStack.EMPTY;
		}
		FluidState fluidState = state.getFluidState();
		if (fluidState.isEmpty() || !fluidState.isSource()) return FluidStack.EMPTY;
		if (!resource.isFluidEqual(new FluidStack(fluidState.getType(), 1000))) return FluidStack.EMPTY;
		BlockState correct = fluidState.getFluidType().getBlockForFluidState(level, pos, fluidState.getType().defaultFluidState());
		if (state != correct) return FluidStack.EMPTY;
		if (action.execute()) {
			level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
		return new FluidStack(fluidState.getType(), 1000);
	}

	@Override
	public @NotNull
	FluidStack drain(int maxDrain, FluidAction action) {
		if (maxDrain < FluidType.BUCKET_VOLUME) {
			return FluidStack.EMPTY;
		}
		FluidState fluidState = state.getFluidState();
		if (fluidState.isEmpty() || !fluidState.isSource()) return FluidStack.EMPTY;
		BlockState correct = fluidState.getFluidType().getBlockForFluidState(level, pos, fluidState.getType().defaultFluidState());
		if (state != correct) return FluidStack.EMPTY;
		if (action.execute()) {
			level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
		return new FluidStack(fluidState.getType(), 1000);
	}

}
