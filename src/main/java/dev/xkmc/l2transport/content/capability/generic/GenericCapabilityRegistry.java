package dev.xkmc.l2transport.content.capability.generic;

import dev.xkmc.l2library.util.code.Wrappers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.TreeMap;

public class GenericCapabilityRegistry {

	private static final TreeMap<ResourceLocation, ICapabilityEntry<?>> CAPABILITIES = new TreeMap<>();

	public static <E extends ICapabilityEntry<T>, T> void reg(E cap) {
		CAPABILITIES.put(cap.id(), cap);
	}

	public static Collection<ICapabilityEntry<?>> values() {
		return CAPABILITIES.values();
	}

	public static <T> Optional<ICapabilityEntry<T>> getCapType(Capability<T> cap) {
		return Wrappers.cast(values().stream().filter(t -> t.cap() == cap).findFirst());
	}

	public static ICapabilityEntry<?> getOrDefault(@Nullable ResourceLocation id) {
		return id == null || !CAPABILITIES.containsKey(id) ? CAPABILITIES.firstEntry().getValue() : CAPABILITIES.get(id);
	}

	public static ResourceLocation next(@Nullable ResourceLocation id) {
		if (id == null) return CAPABILITIES.firstKey();
		ResourceLocation ans = CAPABILITIES.higherKey(id);
		if (ans == null) return CAPABILITIES.firstKey();
		return ans;
	}

}
