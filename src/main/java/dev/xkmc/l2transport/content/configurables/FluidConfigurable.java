package dev.xkmc.l2transport.content.configurables;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.fluid.IFluidNodeBlockEntity;
import dev.xkmc.l2transport.init.data.LTModConfig;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.fluids.FluidStack;

@SerialClass
public class FluidConfigurable extends BaseConfigurable implements IConfigurableFilter {

	@SerialClass.SerialField(toClient = true)
	private FluidStack filter = FluidStack.EMPTY;

	@SerialClass.SerialField(toClient = true)
	protected boolean match_tag = false;

	public FluidConfigurable(ConfigConnectorType type, IFluidNodeBlockEntity node) {
		super(type, node);
	}

	public boolean isFluidStackValid(FluidStack stack) {
		if (filter.isEmpty()) return true;
		if (match_tag) return stack.isFluidEqual(filter);
		return stack.getFluid() == filter.getFluid();
	}

	public FluidStack getDisplayFluid() {
		return filter;
	}

	@Override
	public MutableComponent getFilterDesc() {
		return filter.getDisplayName().copy();
	}

	@Override
	public boolean shouldDisplay() {
		return !filter.isEmpty();
	}

	public void clearFilter() {
		filter = FluidStack.EMPTY;
	}

	public boolean hasNoFilter() {
		return filter.isEmpty();
	}

	public void setSimpleFilter(FluidStack copy) {
		filter = copy;
	}

	public boolean canQuickSetCount(FluidStack stack) {
		return type.canSetCount() && shouldDisplay() && filter.getFluid() == stack.getFluid();
	}

	@Override
	protected int getTypeDefaultMax() {
		return LTModConfig.COMMON.defaultFluidPacket.get();
	}

}
