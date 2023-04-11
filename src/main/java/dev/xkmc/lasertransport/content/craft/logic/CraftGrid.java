package dev.xkmc.lasertransport.content.craft.logic;

import dev.xkmc.lasertransport.content.craft.logic.CraftItemSlot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record CraftGrid(int height, int width, CraftItemSlot[][] list) implements TooltipComponent {

}
