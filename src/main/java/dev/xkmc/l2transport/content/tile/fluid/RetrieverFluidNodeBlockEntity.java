package dev.xkmc.l2transport.content.tile.fluid;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.fluid.CauldronFluidHandler;
import dev.xkmc.l2transport.content.connector.ExtractConnector;
import dev.xkmc.l2transport.content.connector.IConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

@SerialClass
public class RetrieverFluidNodeBlockEntity extends AbstractFluidNodeBlockEntity<RetrieverFluidNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final ExtractConnector connector = new ExtractConnector(this::getMaxCoolDown, this::getMaxTransfer,
			() -> getBlockPos().relative(getBlockState().getValue(BlockStateProperties.FACING)));

	public RetrieverFluidNodeBlockEntity(BlockEntityType<RetrieverFluidNodeBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public IConnector getConnector() {
		return connector;
	}

	@Override
	public void tick() {
		if (level != null && !level.isClientSide() && isReady() && connector.mayExtract()) {
			Direction facing = getBlockState().getValue(BlockStateProperties.FACING);
			BlockPos next = getBlockPos().relative(facing);
			BlockEntity target = level.getBlockEntity(next);
			if (target != null) {
				var lazyCap = target.getCapability(ForgeCapabilities.FLUID_HANDLER, facing.getOpposite());
				if (lazyCap.resolve().isPresent()) {
					var cap = lazyCap.resolve().get();
					connector.performExtract(tryRetrieve(cap));
					markDirty();
				}
			} else {
				BlockState state = level.getBlockState(next);
				if (state.is(Blocks.CAULDRON)) {
					connector.performExtract(tryRetrieve(new CauldronFluidHandler(level, next, state)));
					markDirty();
				}
			}

		}
		super.tick();
	}

	protected boolean tryRetrieve(IFluidHandler target) {
		for (int i = 0; i < target.getTanks(); i++) {
			FluidStack toDrain = target.drain(getMaxTransfer(), IFluidHandler.FluidAction.SIMULATE);
			if (toDrain.isEmpty()) continue;
			if (!getFluid().isEmpty()) {
				if (!isFluidStackValid(toDrain)) continue;
				if (toDrain.getAmount() < getFluid().getAmount()) continue;
			}
			int toFill = getHandler().fill(toDrain, IFluidHandler.FluidAction.SIMULATE);
			if (toFill == 0) continue;
			while (toFill != toDrain.getAmount()) {
				toDrain = target.drain(toFill, IFluidHandler.FluidAction.SIMULATE);
				toFill = getHandler().fill(toDrain, IFluidHandler.FluidAction.SIMULATE);
			}
			if (toFill == 0) continue;
			toDrain = target.drain(toFill, IFluidHandler.FluidAction.EXECUTE);
			getHandler().fill(toDrain, IFluidHandler.FluidAction.EXECUTE);
			return true;
		}
		return false;
	}

}
