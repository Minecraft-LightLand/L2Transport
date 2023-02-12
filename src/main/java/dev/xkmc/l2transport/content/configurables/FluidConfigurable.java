package dev.xkmc.l2transport.content.configurables;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.fluid.IFluidNodeBlockEntity;
import dev.xkmc.l2transport.content.menu.container.FluidContainer;
import dev.xkmc.l2transport.content.menu.container.ReadOnlyContainer;
import dev.xkmc.l2transport.content.menu.filter.FluidConfigMenu;
import dev.xkmc.l2transport.init.data.LTModConfig;
import dev.xkmc.l2transport.init.data.LangData;
import dev.xkmc.l2transport.init.registrate.LTMenus;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class FluidConfigurable extends CommonConfigurable<FluidStack> implements MenuProvider, FluidContainer, ReadOnlyContainer {

	@SerialClass.SerialField(toClient = true)
	private final ArrayList<FluidStack> filters = new ArrayList<>();

	public FluidConfigurable(ConfigConnectorType type, IFluidNodeBlockEntity node) {
		super(type, node);
	}

	@Override
	public List<FluidStack> getFilters() {
		return filters;
	}

	public boolean internalMatch(FluidStack stack) {
		for (FluidStack filter : filters) {
			if (stack.getFluid() == filter.getFluid()) {
				if (match_tag) {
					if (stack.isFluidEqual(filter)) {
						return true;
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}

	public FluidStack getDisplayFluid() {
		return filters.size() == 1 ? filters.get(0) : FluidStack.EMPTY;
	}

	@Override
	public MutableComponent getFilterDesc() {
		return filters.size() == 1 ? filters.get(0).getDisplayName().copy() : LangData.CONFIG_FILTER.get();
	}

	public boolean canQuickSetCount(FluidStack stack) {
		if (isLocked()) return false;
		return type.canSetCount() && shouldDisplay() && filters.size() == 1 && filters.get(0).getFluid() == stack.getFluid();
	}

	@Override
	protected int getTypeDefaultMax() {
		return LTModConfig.COMMON.defaultFluidPacket.get();
	}

	@Override
	public NumericAdjustor getTransferConfig() {
		return new NumericAdjustor(NumericConfigurator.FLUID, this);
	}

	public FluidStack getFluid(int slot) {
		return slot < 0 || slot >= filters.size() ? FluidStack.EMPTY : filters.get(slot);
	}

	@Nullable
	@Override
	public MenuProvider getMenu() {
		if (in_use) return null;
		return this;
	}

	@Override
	public int getContainerSize() {
		return FluidConfigMenu.SIZE;
	}

	@Override
	public boolean isEmpty() {
		return filters.isEmpty();
	}

	@Override
	public ItemStack getItem(int slot) {
		return getItemImpl(slot);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable(LTMenus.getLangKey(LTMenus.MT_FLUID.get()));
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int wid, Inventory inv, Player player) {
		return new FluidConfigMenu(LTMenus.MT_FLUID.get(), wid, inv, this, node.getThis().getBlockPos());
	}

}
