package dev.xkmc.l2transport.content.tile.client;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.*;

public class TooltipBuilder {

	private final TreeMap<TooltipType, List<Component>> map = new TreeMap<>(Comparator.comparingInt(Enum::ordinal));

	public void add(TooltipType type, MutableComponent comp) {
		map.computeIfAbsent(type, k -> new ArrayList<>()).add(comp);
	}

	public List<Component> build() {
		return map.values().stream().flatMap(Collection::stream).toList();
	}
}
