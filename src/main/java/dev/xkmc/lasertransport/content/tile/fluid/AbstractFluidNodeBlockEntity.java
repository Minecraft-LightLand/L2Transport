package dev.xkmc.lasertransport.content.tile.fluid;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.lasertransport.content.capability.fluid.IFluidNodeBlockEntity;
import dev.xkmc.lasertransport.content.capability.fluid.NodalFluidHandler;
import dev.xkmc.lasertransport.content.capability.generic.ForgeCapabilityHolder;
import dev.xkmc.lasertransport.content.capability.generic.ICapabilityHolder;
import dev.xkmc.lasertransport.content.items.upgrades.LevelDropUpgrade;
import dev.xkmc.lasertransport.content.items.upgrades.UpgradeFlag;
import dev.xkmc.lasertransport.content.items.upgrades.UpgradeItem;
import dev.xkmc.lasertransport.content.tile.base.AbstractNodeBlockEntity;
import dev.xkmc.lasertransport.content.tile.base.IRenderableFluidNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
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
		flags.add(UpgradeFlag.LEVEL);
	}

	@Override
	public boolean isTargetValid(BlockPos pos) {
		assert level != null;
		return super.isTargetValid(pos) || level.getBlockState(pos).is(BlockTags.CAULDRONS);
	}

	@Override
	public boolean acceptUpgrade(GenericItemStack<UpgradeItem> item) {
		return super.acceptUpgrade(item) && !(item.item().getUpgrade() instanceof LevelDropUpgrade);
	}

	@Override
	public ICapabilityHolder<?> getValidTarget() {
		return new ForgeCapabilityHolder<>(ForgeCapabilities.FLUID_HANDLER);
	}

	public FluidStack getFluid() {
		return getConfig().getDisplayFluid();
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

}
