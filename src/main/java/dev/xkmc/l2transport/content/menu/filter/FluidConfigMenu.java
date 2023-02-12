package dev.xkmc.l2transport.content.menu.filter;

import dev.xkmc.l2library.base.menu.SpriteManager;
import dev.xkmc.l2library.util.code.Wrappers;
import dev.xkmc.l2transport.content.configurables.FluidConfigurable;
import dev.xkmc.l2transport.content.menu.container.FluidContainer;
import dev.xkmc.l2transport.content.menu.ghost.IFluidConfigMenu;
import dev.xkmc.l2transport.content.menu.ghost.IItemConfigMenu;
import dev.xkmc.l2transport.content.tile.fluid.AbstractFluidNodeBlockEntity;
import dev.xkmc.l2transport.init.L2Transport;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;

public class FluidConfigMenu extends BaseConfigMenu<FluidConfigMenu> implements IItemConfigMenu, IFluidConfigMenu {

	public static final SpriteManager MANAGER = new SpriteManager(L2Transport.MODID, "item_config");
	public static final int SIZE = 18;

	public static FluidConfigMenu fromNetwork(MenuType<FluidConfigMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		BlockPos pos = buf.readBlockPos();
		BlockEntity be = plInv.player.level.getBlockEntity(pos);
		assert be != null;
		AbstractFluidNodeBlockEntity<?> node = Wrappers.cast(be);
		return new FluidConfigMenu(type, wid, plInv, node.getConfig(), pos);
	}

	public FluidConfigMenu(MenuType<FluidConfigMenu> type, int wid, Inventory plInv, FluidContainer container, BlockPos pos) {
		super(type, wid, plInv, MANAGER, container, pos);
		addSlot("grid", e -> true);
	}

	protected FluidConfigurable getConfig() {
		return Wrappers.cast(node.getConfig());
	}

	@Override
	protected ItemStack getSlotContent(int slot) {
		FluidStack fluid = getConfig().getFluid(slot);
		return fluid.isEmpty() || fluid.hasTag() ? ItemStack.EMPTY : fluid.getFluid().getBucket().getDefaultInstance();
	}

	@Override
	public void setSlotContent(int slot, ItemStack stack) {
		if (stack.isEmpty()) {
			removeContent(slot);
			return;
		}
		var opt = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
		if (opt.isPresent() && opt.resolve().isPresent()) {
			var handler = opt.resolve().get();
			if (handler.getTanks() > 0) {
				var fluid = handler.getFluidInTank(0);
				setSlotContent(slot, fluid);
			}
		}
	}

	public void setSlotContent(int slot, FluidStack stack) {
		if (stack.isEmpty()) {
			removeContent(slot);
		} else if (getConfig().getFluid(slot).isEmpty()) {
			tryAddContent(stack);
		} else {
			if (getConfig().internalMatch(stack)) return;
			getConfig().getFilters().set(slot, stack);
			updateBlock();
		}
	}

	@Override
	protected void tryAddContent(ItemStack stack) {
		var opt = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
		if (opt.isPresent() && opt.resolve().isPresent()) {
			var handler = opt.resolve().get();
			if (handler.getTanks() > 0) {
				var fluid = handler.getFluidInTank(0);
				if (!fluid.isEmpty()) {
					tryAddContent(fluid);
				}
			}
		}
	}

	protected void tryAddContent(FluidStack stack) {
		if (getConfig().getFilters().size() < FluidConfigMenu.SIZE) {
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

}
