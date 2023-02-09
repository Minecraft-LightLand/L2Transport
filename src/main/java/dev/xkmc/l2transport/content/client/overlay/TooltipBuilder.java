package dev.xkmc.l2transport.content.client.overlay;

import net.minecraft.client.gui.screens.Screen;
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
		return map.entrySet().stream().filter(e -> Screen.hasShiftDown() || e.getKey() != TooltipType.DESC).flatMap(e -> e.getValue().stream()).toList();
	}
}
