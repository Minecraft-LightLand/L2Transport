package dev.xkmc.l2transport.content.tile.fluid;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.fluid.FluidNodeEntity;
import dev.xkmc.l2transport.content.fluid.NodalFluidHandler;
import dev.xkmc.l2transport.content.item.NodalItemHandler;
import dev.xkmc.l2transport.content.tile.base.AbstractNodeBlockEntity;
import dev.xkmc.l2transport.content.tile.base.IRenderableFluidNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SerialClass
public abstract class AbstractFluidNodeBlockEntity<BE extends AbstractFluidNodeBlockEntity<BE>> extends AbstractNodeBlockEntity<BE>
		implements FluidNodeEntity, IRenderableFluidNode {

	protected final LazyOptional<NodalFluidHandler> fluidHandler = LazyOptional.of(() -> new NodalFluidHandler(this));

	public AbstractFluidNodeBlockEntity(BlockEntityType<BE> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@SerialClass.SerialField(toClient = true)
	public FluidStack filter = FluidStack.EMPTY;

	@Override
	public int getMaxTransfer() {
		return 8000;
	}

	public FluidStack getFluid() {
		return filter;
	}

	public final boolean isFluidStackValid(FluidStack stack) {
		if (filter.isEmpty()) {
			return true;
		}
		return stack.isFluidEqual(filter);
	}

	@Override
	public @NotNull <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return fluidHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	protected NodalFluidHandler getHandler() {
		return fluidHandler.resolve().get();
	}

}
