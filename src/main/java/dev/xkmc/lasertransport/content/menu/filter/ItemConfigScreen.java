package dev.xkmc.lasertransport.content.menu.filter;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ItemConfigScreen extends BaseConfigScreen<ItemConfigMenu> {

	public ItemConfigScreen(ItemConfigMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

}
