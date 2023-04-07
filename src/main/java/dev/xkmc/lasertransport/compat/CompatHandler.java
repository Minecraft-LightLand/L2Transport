package dev.xkmc.lasertransport.compat;

import dev.xkmc.lasertransport.compat.ars_nouveau.SourceHolder;
import dev.xkmc.lasertransport.compat.botania.ManaHolder;
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
		if (ModList.get().isLoaded("ars_nouveau")) {
			GenericCapabilityRegistry.reg(SourceHolder.SOURCE);
		}
		if (ModList.get().isLoaded("botania")) {
			GenericCapabilityRegistry.reg(ManaHolder.MANA);
		}
	}

}
