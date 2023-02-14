package dev.xkmc.lasertransport.compat.energy;

import dev.xkmc.lasertransport.content.capability.generic.NodalGenericHandler;
import dev.xkmc.lasertransport.content.flow.TransportHandler;
import net.minecraftforge.energy.IEnergyStorage;

public record NodalEnergyHandler(NodalGenericHandler node) implements IEnergyStorage {

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		long max = Math.min(maxReceive, node.entity().getConfig().getMaxTransfer());
		return (int) TransportHandler.insert(node, EnergyHolder.ENERGY.empty().getCopy(max), simulate);
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
