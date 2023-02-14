package dev.xkmc.lasertransport.compat.energy;

import dev.xkmc.lasertransport.content.capability.generic.GenericHolder;
import dev.xkmc.lasertransport.content.capability.generic.HandlerWrapper;
import net.minecraftforge.energy.IEnergyStorage;

public record EnergyWrapper(IEnergyStorage be) implements HandlerWrapper {

	@Override
	public long insert(GenericHolder token, boolean simulate) {
		return be.receiveEnergy((int) token.amount(), simulate);
	}

	@Override
	public GenericHolder extract(int slot, long max, boolean b) {
		return EnergyHolder.ENERGY.empty().getCopy(be.extractEnergy((int) max, true));
	}

	@Override
	public int getSize() {
		return 1;
	}

}
