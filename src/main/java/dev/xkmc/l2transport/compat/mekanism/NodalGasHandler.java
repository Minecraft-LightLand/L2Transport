package dev.xkmc.l2transport.compat.mekanism;

import dev.xkmc.l2transport.content.capability.generic.NodalGenericHandler;
import dev.xkmc.l2transport.content.flow.TransportHandler;
import mekanism.api.Action;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;

public record NodalGasHandler(NodalGenericHandler node) implements IGasHandler {

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
		return node().entity().getMaxTransfer();
	}

	@Override
	public boolean isValid(int i, GasStack stack) {
		return node().entity().isContentValid(GasHolder.parse(stack));
	}

	@Override
	public GasStack insertChemical(int i, GasStack stack, Action action) {
		long insert = TransportHandler.insert(node, GasHolder.parse(stack), action.simulate());
		return insert == 0 ? GasStack.EMPTY : new GasStack(stack.getType(), insert);
	}

	@Override
	public GasStack extractChemical(int i, long l, Action action) {
		return GasStack.EMPTY;
	}
}
