package dev.xkmc.lasertransport.compat.mekanism;

import dev.xkmc.lasertransport.content.capability.generic.INodeHandlerWrapper;
import dev.xkmc.lasertransport.content.capability.generic.NodalGenericHandler;
import dev.xkmc.lasertransport.content.flow.TransportHandler;
import mekanism.api.Action;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;

public record NodalGasHandler(NodalGenericHandler node) implements INodeHandlerWrapper, IGasHandler {

	@Override
	public int getTanks() {
		return 1;
	}

	@Override
	public GasStack getChemicalInTank(int i) {
		return GasStack.EMPTY;
	}

	@Override
	public void setChemicalInTank(int i, GasStack stack) {

	}

	@Override
	public long getTankCapacity(int i) {
		return node().entity().getConfig().getMaxTransfer();
	}

	@Override
	public boolean isValid(int i, GasStack stack) {
		return node().entity().getConfig().isContentValid(GasHolder.parse(stack));
	}

	@Override
	public GasStack insertChemical(int i, GasStack stack, Action action) {
		long max = Math.min(stack.getAmount(), node.entity().getConfig().getMaxTransfer());
		GasStack copy = stack.copy();
		copy.setAmount(max);
		long insert = TransportHandler.insert(node, GasHolder.parse(copy), action.simulate());
		long remain = max - insert;
		GasStack ans = stack.copy();
		ans.setAmount(remain);
		return remain == 0 ? GasStack.EMPTY : ans;
	}

	@Override
	public GasStack extractChemical(int i, long l, Action action) {
		return GasStack.EMPTY;
	}
}
