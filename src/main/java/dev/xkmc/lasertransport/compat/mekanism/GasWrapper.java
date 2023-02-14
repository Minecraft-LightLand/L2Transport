package dev.xkmc.lasertransport.compat.mekanism;

import dev.xkmc.lasertransport.content.capability.generic.GenericHolder;
import dev.xkmc.lasertransport.content.capability.generic.HandlerWrapper;
import mekanism.api.Action;
import mekanism.api.chemical.gas.IGasHandler;

public record GasWrapper(IGasHandler be) implements HandlerWrapper {

	@Override
	public long insert(GenericHolder token, boolean simulate) {
		long val = be.insertChemical(GasHolder.parse(token), Action.get(!simulate)).getAmount();
		return token.getCount() - val;
	}

	@Override
	public GenericHolder extract(int slot, long max, boolean simulate) {
		return GasHolder.parse(be.extractChemical(slot, max, Action.get(!simulate)));
	}

	@Override
	public int getSize() {
		return 1;
	}

}
