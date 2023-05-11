package dev.xkmc.lasertransport.compat;

import dev.xkmc.lasertransport.compat.energy.EnergyHolder;
import dev.xkmc.lasertransport.content.capability.generic.GenericCapabilityRegistry;
import net.minecraftforge.fml.ModList;

public class CompatHandler {

	public static void register() {
		GenericCapabilityRegistry.reg(EnergyHolder.ENERGY);
		if (ModList.get().isLoaded("mekanism")) {
			//TODO GenericCapabilityRegistry.reg(GasHolder.GAS);
		}
		if (ModList.get().isLoaded("ars_nouveau")) {
			//TODO GenericCapabilityRegistry.reg(SourceHolder.SOURCE);
		}
		if (ModList.get().isLoaded("botania")) {
			// TODO GenericCapabilityRegistry.reg(ManaHolder.MANA);
		}
	}

}
