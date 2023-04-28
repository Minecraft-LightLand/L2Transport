package dev.xkmc.lasertransport.content.capability.base;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface PopContentTile {

	List<ItemStack> popContents();

	void markDirty();

}
