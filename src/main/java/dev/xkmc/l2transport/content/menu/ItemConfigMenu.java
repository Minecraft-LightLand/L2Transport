package dev.xkmc.l2transport.content.menu;

import dev.xkmc.l2library.base.menu.SpriteManager;
import dev.xkmc.l2transport.init.L2Transport;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class ItemConfigMenu extends BaseConfigMenu<ItemConfigMenu> {

	public static final SpriteManager MANAGER = new SpriteManager(L2Transport.MODID, "item_config");

	protected ItemConfigMenu(MenuType<?> type, int wid, Inventory plInv) {
		super(type, wid, plInv, MANAGER, e -> new SimpleContainer(18));
	}

	@Override
	public boolean stillValid(Player player) {
		return super.stillValid(player);
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
	}

	@Override
	protected ItemStack getSlotContent(int slot) {
		return null;
	}

	@Override
	protected void setSlotContent(int slot, ItemStack stack) {

	}

	@Override
	protected void tryAddContent(ItemStack stack) {

	}

	@Override
	protected void removeContent(int slot) {

	}
}
