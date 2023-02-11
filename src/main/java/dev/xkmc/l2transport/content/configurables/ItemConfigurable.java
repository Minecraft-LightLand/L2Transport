package dev.xkmc.l2transport.content.configurables;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.item.IItemNodeBlockEntity;
import dev.xkmc.l2transport.content.menu.container.ReadOnlyContainer;
import dev.xkmc.l2transport.content.menu.filter.ItemConfigMenu;
import dev.xkmc.l2transport.init.data.LangData;
import dev.xkmc.l2transport.init.registrate.LTMenus;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class ItemConfigurable extends CommonConfigurable<ItemStack> implements MenuProvider, ReadOnlyContainer {

	@SerialClass.SerialField(toClient = true)
	private final ArrayList<ItemStack> filters = new ArrayList<>();

	@SerialClass.SerialField(toClient = true)
	protected boolean match_tag = false;

	public ItemConfigurable(ConfigConnectorType type, IItemNodeBlockEntity node) {
		super(type, node);
	}

	@Override
	public List<ItemStack> getFilters() {
		return filters;
	}

	public boolean internalMatch(ItemStack stack) {
		for (ItemStack filter : filters) {
			if (stack.getItem() == filter.getItem()) {
				if (match_tag) {
					if (ItemStack.isSameItemSameTags(stack, filter)) {
						return true;
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}

	public ItemStack getDisplayItem() {
		return filters.size() == 1 ? filters.get(0) : ItemStack.EMPTY;
	}

	public MutableComponent getFilterDesc() {
		return filters.size() == 1 ? filters.get(0).getHoverName().copy() : LangData.CONFIG_FILTER.get();
	}

	public boolean canQuickSetCount(ItemStack stack) {
		if (isLocked()) return false;
		return type.canSetCount() && shouldDisplay() && filters.size() == 1 && filters.get(0).getItem() == stack.getItem();
	}

	@Override
	protected int getTypeDefaultMax() {
		return 64;
	}

	@Override
	public NumericAdjustor getTransferConfig() {
		return new NumericAdjustor(NumericConfigurator.ITEM, this);
	}

	@Nullable
	@Override
	public MenuProvider getMenu() {
		if (in_use) return null;
		return this;
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable(LTMenus.getLangKey(LTMenus.MT_ITEM.get()));
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int wid, Inventory inv, Player player) {
		return new ItemConfigMenu(LTMenus.MT_ITEM.get(), wid, inv, this, node.getThis().getBlockPos());
	}

	@Override
	public int getContainerSize() {
		return ItemConfigMenu.SIZE;
	}

	@Override
	public boolean isEmpty() {
		return getFilters().isEmpty();
	}

	@Override
	public ItemStack getItem(int ind) {
		return ind >= 0 && ind < filters.size() ? filters.get(ind) : ItemStack.EMPTY;
	}

}
