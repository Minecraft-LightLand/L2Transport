package dev.xkmc.l2transport.compat.energy;

import dev.xkmc.l2transport.content.capability.generic.GenericHolder;
import dev.xkmc.l2transport.content.capability.generic.HandlerWrapper;
import net.minecraftforge.energy.IEnergyStorage;

public record EnergyWrapper(IEnergyStorage be) implements HandlerWrapper {

	@Override
	public int insert(GenericHolder token, boolean simulate) {
		return be.receiveEnergy(token.amount(), simulate);
	}

	@Override
	public GenericHolder extract(int slot, int max, boolean b) {
		return EnergyHolder.ENERGY.empty().getCopy(be.extractEnergy(max, true));
	}

	@Override
	public int getSize() {
		return 1;
	}

}
