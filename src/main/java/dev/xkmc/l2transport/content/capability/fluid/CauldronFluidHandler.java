package dev.xkmc.l2transport.content.capability.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public record CauldronFluidHandler(Level level, BlockPos pos, BlockState state) implements IFluidHandler {

	@Override
	public int getTanks() {
		return 1;
	}

	@Override
	public @NotNull
	FluidStack getFluidInTank(int tank) {
		if (state == Blocks.LAVA_CAULDRON.defaultBlockState())
			return new FluidStack(Fluids.LAVA, FluidType.BUCKET_VOLUME);
		if (state == Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3))
			return new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME);
		return FluidStack.EMPTY;
	}

	@Override
	public int getTankCapacity(int tank) {
		return FluidType.BUCKET_VOLUME;
	}

	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
		return stack.getFluid() == Fluids.LAVA || stack.getFluid() == Fluids.WATER;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if (state == Blocks.CAULDRON.defaultBlockState()) {
			if (action.execute()) {
				BlockState state;
				if (resource.getFluid() == Fluids.LAVA) {
					state = Blocks.LAVA_CAULDRON.defaultBlockState();
				} else {
					state = Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3);
				}
				level.setBlockAndUpdate(pos, state);
			}
			return FluidType.BUCKET_VOLUME;
		}
		return 0;
	}

	@Override
	public @NotNull
	FluidStack drain(FluidStack resource, FluidAction action) {
		if (resource.getAmount() < FluidType.BUCKET_VOLUME) {
			return FluidStack.EMPTY;
		}
		FluidStack stack;
		if (state == Blocks.LAVA_CAULDRON.defaultBlockState())
			stack = new FluidStack(Fluids.LAVA, FluidType.BUCKET_VOLUME);
		else if (state == Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3))
			stack = new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME);
		else stack = FluidStack.EMPTY;
		if (resource.getFluid() != stack.getFluid()) {
			return FluidStack.EMPTY;
		}
		if (action.execute()) {
			level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
		}
		return stack;
	}

	@Override
	public @NotNull
	FluidStack drain(int maxDrain, FluidAction action) {
		if (maxDrain < FluidType.BUCKET_VOLUME) {
			return FluidStack.EMPTY;
		}
		FluidStack stack;
		if (state == Blocks.LAVA_CAULDRON.defaultBlockState())
			stack = new FluidStack(Fluids.LAVA, FluidType.BUCKET_VOLUME);
		else if (state == Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3))
			stack = new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME);
		else stack = FluidStack.EMPTY;
		if (action.execute()) {
			level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
		}
		return stack;
	}

}
