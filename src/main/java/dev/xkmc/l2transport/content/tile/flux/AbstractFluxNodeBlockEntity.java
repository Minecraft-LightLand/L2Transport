package dev.xkmc.l2transport.content.tile.flux;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.generic.*;
import dev.xkmc.l2transport.content.tile.base.AbstractNodeBlockEntity;
import dev.xkmc.l2transport.content.tile.base.IRenderableNode;
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
	public boolean isContentValid(GenericHolder stack) {
		return stack.type() == getCapType();
	}

	@Override
	public ICapabilityEntry<?> getCapType() {
		return GenericCapabilityRegistry.ENERGY;
	}

	@Override
	public int getMaxTransfer() {
		return getCapType().getDefaultMax();
	}

	@Override
	public List<MutableComponent> getTooltips() {
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
