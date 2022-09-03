package dev.xkmc.l2transport.content.capability.generic;

import dev.xkmc.l2library.util.code.Wrappers;
import dev.xkmc.l2transport.content.capability.energy.EnergyHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import java.util.Collection;
import java.util.Optional;
import java.util.TreeMap;

public class GenericCapabilityRegistry {

	private static final TreeMap<ResourceLocation, ICapabilityEntry<?>> CAPABILITIES = new TreeMap<>();

	public static final EnergyHolder ENERGY;

	static {
		ENERGY = reg(new EnergyHolder());
	}

	public static <E extends ICapabilityEntry<T>, T> E reg(E cap) {
		CAPABILITIES.put(cap.id(), cap);
		return cap;
	}

	public static Collection<ICapabilityEntry<?>> values() {
		return CAPABILITIES.values();
	}

	public static void register() {
	}

	public static <T> Optional<ICapabilityEntry<T>> getCapType(Capability<T> cap) {
		return Wrappers.cast(values().stream().filter(t -> t.cap() == cap).findFirst());
	}

}
