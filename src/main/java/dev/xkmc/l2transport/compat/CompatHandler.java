package dev.xkmc.l2transport.compat;

import dev.xkmc.l2transport.compat.energy.EnergyHolder;
import dev.xkmc.l2transport.compat.mekanism.GasHolder;
import dev.xkmc.l2transport.content.capability.generic.GenericCapabilityRegistry;
import net.minecraftforge.fml.ModList;

public class CompatHandler {

	public static void register() {
		GenericCapabilityRegistry.reg(EnergyHolder.ENERGY);
		if (ModList.get().isLoaded("mekanism")) {
			GenericCapabilityRegistry.reg(GasHolder.GAS);
		}
	}

}
