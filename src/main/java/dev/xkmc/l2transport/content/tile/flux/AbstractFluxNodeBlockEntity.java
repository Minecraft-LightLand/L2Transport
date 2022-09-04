package dev.xkmc.l2transport.content.tile.flux;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.compat.energy.EnergyHolder;
import dev.xkmc.l2transport.content.capability.generic.GenericHolder;
import dev.xkmc.l2transport.content.capability.generic.ICapabilityEntry;
import dev.xkmc.l2transport.content.capability.generic.IGenericNodeBlockEntity;
import dev.xkmc.l2transport.content.capability.generic.NodalGenericHandler;
import dev.xkmc.l2transport.content.tile.base.AbstractNodeBlockEntity;
import dev.xkmc.l2transport.content.tile.base.IRenderableNode;
import dev.xkmc.l2transport.content.tile.client.TooltipBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SerialClass
public abstract class AbstractFluxNodeBlockEntity<BE extends AbstractFluxNodeBlockEntity<BE>> extends AbstractNodeBlockEntity<BE>
		implements IGenericNodeBlockEntity, IRenderableNode {

	protected final NodalGenericHandler genericHandler = new NodalGenericHandler(this);

	public AbstractFluxNodeBlockEntity(BlockEntityType<BE> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public Capability<?> getValidTarget() {
		return getCapType().cap();
	}

	@Override
	public boolean isContentValid(GenericHolder stack) {
		return stack.type() == getCapType();
	}

	@Override
	public ICapabilityEntry<?> getCapType() {
		return EnergyHolder.ENERGY;
	}

	@Override
	public int getMaxTransfer() {
		return getCapType().getDefaultMax();
	}

	@Override
	public TooltipBuilder getTooltips() {
		var ans = super.getTooltips();
		getConnector().addTooltips(ans, getCapType().empty());
		return ans;
	}

	@Override
	public @NotNull <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
		if (cap == getCapType().cap()) {
			return LazyOptional.of(() -> getCapType().parseHandler(genericHandler)).cast();
		}
		return super.getCapability(cap, side);
	}

	protected int getLimit() {
		return getCapType().getDefaultMax();
	}

}
