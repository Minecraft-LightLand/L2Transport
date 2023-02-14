package dev.xkmc.lasertransport.compat;

import dev.xkmc.lasertransport.compat.energy.EnergyHolder;
import dev.xkmc.lasertransport.compat.mekanism.GasHolder;
import dev.xkmc.lasertransport.content.capability.generic.GenericCapabilityRegistry;
import net.minecraftforge.fml.ModList;

public class CompatHandler {

	public static void register() {
		GenericCapabilityRegistry.reg(EnergyHolder.ENERGY);
		if (ModList.get().isLoaded("mekanism")) {
			GenericCapabilityRegistry.reg(GasHolder.GAS);
		}
	}

}
