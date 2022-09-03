package dev.xkmc.l2transport.content.capability.fluid;

import dev.xkmc.l2transport.content.capability.base.FluidStackNode;
import dev.xkmc.l2transport.content.capability.base.SimpleNodeSupplier;
import dev.xkmc.l2transport.content.flow.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record NodalFluidHandler(IFluidNodeBlockEntity be) implements IFluidHandler, FluidStackNode {

	@Override
	public int getTanks() {
		return 1;
	}

	@Override
	public @NotNull FluidStack getFluidInTank(int tank) {
		return FluidStack.EMPTY;
	}

	@Override
	public int getTankCapacity(int tank) {
		return be.getMaxTransfer();
	}

	@Override
	public boolean isFluidValid(int slot, @NotNull FluidStack stack) {
		return be.isFluidStackValid(stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		return TransportHandler.insert(this, new FluidHolder(resource), action.simulate());
	}

	@Override
	public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
		return FluidStack.EMPTY;
	}

	@Override
	public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
		return FluidStack.EMPTY;
	}

	@Override
	public NetworkType getNetworkType() {
		return be.getConnector();
	}

	@Override
	public boolean isValid(IContentHolder<FluidStack> token) {
		return be.isFluidStackValid(token.get());
	}

	@Override
	public List<INodeSupplier<FluidStack>> getTargets() {
		Level level = be.getLevel();
		if (level == null) return List.of();
		List<INodeSupplier<FluidStack>> ans = new ArrayList<>();
		for (BlockPos pos : be.getConnector().getAvailableTarget()) {
			BlockEntity target = level.getBlockEntity(pos);
			if (target != null) {
				var lazyCap = target.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
				if (lazyCap.resolve().isPresent()) {
					var cap = lazyCap.resolve().get();
					if (cap instanceof FluidStackNode node) {
						ans.add(new SimpleNodeSupplier<>(pos, node.isReady(), (ctx, token) -> TransportHandler.broadcastRecursive(ctx, node, token)));
					} else {
						ans.add(new SimpleNodeSupplier<>(pos, true, (ctx, token) -> new FluidNodeTarget(target, cap, token)));
					}
					continue;
				}
			}
			ans.add(new SimpleNodeSupplier<>(pos, false, (ctx, token) -> new ErrorNode<>(pos)));
		}
		return ans;
	}

	@Override
	public void refreshCoolDown(BlockPos target, boolean success, TransportContext<FluidStack> ctx) {
		be.refreshCoolDown(target, success, ctx.simulate);
	}

	@Override
	public BlockPos getIdentifier() {
		return be.getBlockPos();
	}

	@Override
	public boolean isReady() {
		return be.getConnector().isReady();
	}

}
