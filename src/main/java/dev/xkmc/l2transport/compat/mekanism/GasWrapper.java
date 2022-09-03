package dev.xkmc.l2transport.compat.mekanism;

import dev.xkmc.l2transport.content.capability.generic.GenericHolder;
import dev.xkmc.l2transport.content.capability.generic.HandlerWrapper;
import mekanism.api.Action;
import mekanism.api.chemical.gas.IGasHandler;

public record GasWrapper(IGasHandler be) implements HandlerWrapper {

	@Override
	public int insert(GenericHolder token, boolean simulate) {
		return (int) be.insertChemical(GasHolder.parse(token), Action.get(!simulate)).getAmount();
	}

	@Override
	public GenericHolder extract(int slot, int max, boolean simulate) {
		return GasHolder.parse(be.extractChemical(slot, max, Action.get(!simulate)));
	}

	@Override
	public int getSize() {
		return 1;
	}

}
