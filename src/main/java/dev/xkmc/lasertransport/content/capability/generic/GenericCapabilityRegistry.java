package dev.xkmc.lasertransport.content.capability.generic;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

public class GenericCapabilityRegistry {

	private static final TreeMap<ResourceLocation, ICapabilityEntry<?>> CAPABILITIES = new TreeMap<>();

	public static <E extends ICapabilityEntry<T>, T> void reg(E cap) {
		CAPABILITIES.put(cap.id(), cap);
	}

	public static Collection<ICapabilityEntry<?>> values() {
		return CAPABILITIES.values();
	}

	public static ICapabilityEntry<?> getOrDefault(@Nullable ResourceLocation id) {
		return id == null || !CAPABILITIES.containsKey(id) ? CAPABILITIES.firstEntry().getValue() : CAPABILITIES.get(id);
	}

	public static int indexOf(ICapabilityEntry<?> entry) {
		return new ArrayList<>(values()).indexOf(entry);
	}
}
