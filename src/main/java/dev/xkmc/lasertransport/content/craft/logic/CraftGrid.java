package dev.xkmc.lasertransport.content.craft.logic;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record CraftGrid(int height, int width, CraftItemSlot[][] list) implements TooltipComponent {

}
