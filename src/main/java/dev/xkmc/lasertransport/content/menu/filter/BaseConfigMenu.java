package dev.xkmc.lasertransport.content.menu.filter;

import dev.xkmc.l2library.base.menu.PredSlot;
import dev.xkmc.l2library.base.menu.SpriteManager;
import dev.xkmc.lasertransport.content.capability.base.INodeBlockEntity;
import dev.xkmc.lasertransport.content.configurables.CommonConfigurable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.Predicate;

public abstract class BaseConfigMenu<T extends BaseConfigMenu<T>> extends AbstractContainerMenu {

	protected final Inventory inventory;
	protected final SpriteManager sprite;
	protected final Container container;
	protected INodeBlockEntity node;
	protected BlockPos pos;

	private int added = 0;

	protected BaseConfigMenu(MenuType<?> type, int wid, Inventory plInv, SpriteManager manager, Container container, BlockPos pos) {
		super(type, wid);
		this.inventory = plInv;
		this.sprite = manager;
		this.container = container;

		int x = manager.getPlInvX();
		int y = manager.getPlInvY();
		this.bindPlayerInventory(plInv, x, y);

		this.pos = pos;
		if (plInv.player.level.getBlockEntity(pos) instanceof INodeBlockEntity n) {
			if (!n.getConfig().isInUse()) {
				node = n;
				n.getConfig().setInUse(true);
			}
		}
	}

	protected void bindPlayerInventory(Inventory plInv, int x, int y) {
		int k;
		for (k = 0; k < 3; ++k) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(plInv, j + k * 9 + 9, x + j * 18, y + k * 18));
			}
		}
		for (k = 0; k < 9; ++k) {
			this.addSlot(new Slot(plInv, k, x + k * 18, y + 58));
		}

	}

	protected void addSlot(String name, Predicate<ItemStack> pred) {
		this.sprite.getSlot(name, (x, y) -> new PredSlot(this.container, this.added++, x, y, pred), (n, i, j, s) -> addSlot(s));
	}

	protected abstract CommonConfigurable<?> getConfig();

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
			ItemStack stackToInsert = getSlot(index).getItem();
			tryAddContent(stackToInsert);
		} else {
			removeContent(index - 36);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return player.isAlive() && node != null && player.level.getBlockEntity(pos) == node;
	}

	@Override
	public void removed(Player player) {
		if (node != null) {
			node.getConfig().setInUse(false);
		}
		super.removed(player);
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

	protected void updateBlock() {
		if (inventory.player.level.isClientSide)
			return;
		BlockState state = node.getThis().getBlockState();
		if (state.getValue(BlockStateProperties.LIT) != getConfig().shouldDisplay()) {
			state = state.setValue(BlockStateProperties.LIT, getConfig().shouldDisplay());
			Level level = node.getThis().getLevel();
			assert level != null;
			level.setBlockAndUpdate(node.getThis().getBlockPos(), state);
		} else {
			getConfig().getNode().markDirty();
		}
	}

}
