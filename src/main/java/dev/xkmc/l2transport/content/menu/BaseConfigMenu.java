package dev.xkmc.l2transport.content.menu;

import dev.xkmc.l2library.base.menu.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public abstract class BaseConfigMenu<T extends BaseConfigMenu<T>> extends BaseContainerMenu<T> {

	protected BaseConfigMenu(MenuType<?> type, int wid, Inventory plInv, SpriteManager manager, Function<T, SimpleContainer> factory) {
		super(type, wid, plInv, manager, factory, false);
	}

	protected abstract ItemStack getSlotContent(int slot);

	protected abstract void setSlotContent(int slot, ItemStack stack);

	protected abstract void tryAddContent(ItemStack stack);

	protected abstract void removeContent(int slot);

	public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
		if (slotId < 36) {
			super.clicked(slotId, dragType, clickTypeIn, player);
		} else if (clickTypeIn != ClickType.THROW) {
			ItemStack held = getCarried();
			int slot = slotId - 36;
			ItemStack insert;
			if (clickTypeIn == ClickType.CLONE) {
				if (player.isCreative() && held.isEmpty()) {
					insert = getSlotContent(slot).copy();
					insert.setCount(insert.getMaxStackSize());
					setCarried(insert);
				}
			} else {
				if (held.isEmpty()) {
					insert = ItemStack.EMPTY;
				} else {
					insert = held.copy();
					insert.setCount(1);
				}
				setSlotContent(slot, insert);
			}
		}
	}

	public ItemStack quickMoveStack(Player playerIn, int index) {
		if (index < 36) {
			ItemStack stackToInsert = inventory.getItem(index);
			tryAddContent(stackToInsert);
		} else {
			removeContent(index - 36);
		}
		return ItemStack.EMPTY;
	}

}
