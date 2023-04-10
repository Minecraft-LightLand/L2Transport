package dev.xkmc.lasertransport.content.client.tooltip;

import dev.xkmc.lasertransport.content.craft.logic.CraftItemSlot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record CraftTooltip(int height, int width, CraftItemSlot[][] list) implements TooltipComponent {

}
