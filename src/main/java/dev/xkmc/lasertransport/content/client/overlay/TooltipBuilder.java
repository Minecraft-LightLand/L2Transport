package dev.xkmc.lasertransport.content.client.overlay;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

public class TooltipBuilder {

	private final TreeMap<TooltipType, List<Component>> map = new TreeMap<>(Comparator.comparingInt(Enum::ordinal));

	public void add(TooltipType type, MutableComponent comp) {
		map.computeIfAbsent(type, k -> new ArrayList<>()).add(comp);
	}

	public List<Component> build() {
		return map.entrySet().stream().flatMap(e -> e.getValue().stream()).toList();
	}

	public boolean hasShiftDown() {
		return ShiftManager.isAlternate();
	}

}
