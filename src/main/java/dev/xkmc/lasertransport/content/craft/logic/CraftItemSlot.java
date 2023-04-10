package dev.xkmc.lasertransport.content.craft.logic;

import net.minecraft.world.item.ItemStack;

public record CraftItemSlot(CraftSlotType type, ItemStack stack) {
}
