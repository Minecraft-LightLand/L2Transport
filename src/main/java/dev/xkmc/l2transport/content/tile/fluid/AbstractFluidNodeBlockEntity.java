package dev.xkmc.l2transport.content.tile.fluid;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.fluid.FluidHolder;
import dev.xkmc.l2transport.content.capability.fluid.IFluidNodeBlockEntity;
import dev.xkmc.l2transport.content.capability.fluid.NodalFluidHandler;
import dev.xkmc.l2transport.content.tile.base.AbstractNodeBlockEntity;
import dev.xkmc.l2transport.content.tile.base.IRenderableFluidNode;
import dev.xkmc.l2transport.content.tile.client.TooltipBuilder;
import dev.xkmc.l2transport.content.upgrades.Upgrade;
import dev.xkmc.l2transport.content.upgrades.UpgradeFlag;
import dev.xkmc.l2transport.init.data.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SerialClass
public abstract class AbstractFluidNodeBlockEntity<BE extends AbstractFluidNodeBlockEntity<BE>> extends AbstractNodeBlockEntity<BE>
		implements IFluidNodeBlockEntity, IRenderableFluidNode {

	protected final LazyOptional<NodalFluidHandler> fluidHandler = LazyOptional.of(() -> new NodalFluidHandler(this));

	public AbstractFluidNodeBlockEntity(BlockEntityType<BE> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		flags.add(UpgradeFlag.THROUGH_PUT);
	}

	@SerialClass.SerialField(toClient = true)
	public FluidStack filter = FluidStack.EMPTY;

	@Override
	public Capability<?> getValidTarget() {
		return ForgeCapabilities.FLUID_HANDLER;
	}

	@Override
	public int getMaxTransfer() {
		int cd = ModConfig.COMMON.defaultFluidPacket.get();
		for (Upgrade u : getUpgrades()) {
			cd = u.getMaxTransfer(cd);
		}
		return cd;
	}

	public FluidStack getFluid() {
		return filter;
	}

	public final boolean isFluidStackValid(FluidStack stack) {
		if (filter.isEmpty()) {
			return true;
		}
		return stack.isFluidEqual(filter);
	}

	@Override
	public TooltipBuilder getTooltips() {
		var ans = super.getTooltips();
		getConnector().addTooltips(ans, new FluidHolder(getFluid()));
		return ans;
	}

	@Override
	public @NotNull <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.FLUID_HANDLER) {
			return fluidHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	protected NodalFluidHandler getHandler() {
		return fluidHandler.resolve().get();
	}

	protected int getLimit() {
		return getFluid().isEmpty() ? getMaxTransfer() : getFluid().getAmount();
	}

}
