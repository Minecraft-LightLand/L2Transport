package dev.xkmc.l2transport.compat.energy;

import dev.xkmc.l2transport.content.capability.generic.NodalGenericHandler;
import dev.xkmc.l2transport.content.flow.TransportHandler;
import net.minecraftforge.energy.IEnergyStorage;

public record NodalEnergyHandler(NodalGenericHandler node) implements IEnergyStorage {

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return TransportHandler.insert(node, EnergyHolder.ENERGY.empty().getCopy(maxReceive), simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	public int getEnergyStored() {
		return 0;
	}

	@Override
	public int getMaxEnergyStored() {
		return EnergyHolder.ENERGY.getDefaultMax();
	}

	@Override
	public boolean canExtract() {
		return false;
	}

	@Override
	public boolean canReceive() {
		return true;
	}

}
