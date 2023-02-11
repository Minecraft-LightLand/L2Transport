package dev.xkmc.l2transport.content.menu;

import dev.xkmc.l2library.base.menu.SpriteManager;
import dev.xkmc.l2library.util.code.Wrappers;
import dev.xkmc.l2transport.content.configurables.ItemConfigurable;
import dev.xkmc.l2transport.init.L2Transport;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class ItemConfigMenu extends BaseConfigMenu<ItemConfigMenu> {

	public static final SpriteManager MANAGER = new SpriteManager(L2Transport.MODID, "item_config");
	public static final int SIZE = 18;

	public static ItemConfigMenu fromNetwork(MenuType<ItemConfigMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		BlockPos pos = buf.readBlockPos();
		return new ItemConfigMenu(type, wid, plInv, new SimpleContainer(SIZE), pos);
	}

	public ItemConfigMenu(MenuType<ItemConfigMenu> type, int wid, Inventory plInv, Container container, BlockPos pos) {
		super(type, wid, plInv, MANAGER, container, pos);
		addSlot("grid", e -> true);
	}

	protected ItemConfigurable getConfig() {
		return Wrappers.cast(node.getConfig());
	}

	@Override
	protected ItemStack getSlotContent(int slot) {
		return getConfig().getItem(slot);
	}

	@Override
	public void setSlotContent(int slot, ItemStack stack) {
		if (stack.isEmpty()) {
			removeContent(slot);
		} else if (getConfig().getItem(slot).isEmpty()) {
			tryAddContent(stack);
		} else {
			if (getConfig().internalMatch(stack)) return;
			getConfig().getFilters().set(slot, stack);
			updateBlock();
		}
	}

	@Override
	protected void tryAddContent(ItemStack stack) {
		if (getConfig().getFilters().size() < ItemConfigMenu.SIZE) {
			if (getConfig().internalMatch(stack)) return;
			getConfig().getFilters().add(stack);
			updateBlock();
		}
	}

	@Override
	protected void removeContent(int slot) {
		if (slot < 0 || slot >= getConfig().getFilters().size())
			return;
		getConfig().getFilters().remove(slot);
		updateBlock();
	}

	@Override
	public boolean clickMenuButton(Player player, int btn) {
		if (btn == 0)
			getConfig().getToggleConfig().toggleBlacklist();
		if (btn == 1)
			getConfig().getToggleConfig().toggleTagMatch();
		if (btn == 2)
			getConfig().getToggleConfig().toggleLocked();
		return true;
	}

}
