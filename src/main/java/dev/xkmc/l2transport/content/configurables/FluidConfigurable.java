package dev.xkmc.l2transport.content.configurables;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.fluid.IFluidNodeBlockEntity;
import dev.xkmc.l2transport.init.data.LTModConfig;
import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class FluidConfigurable extends CommonConfigurable<FluidStack> {

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

}
