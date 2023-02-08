package dev.xkmc.l2transport.content.tile.flux;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.generic.*;
import dev.xkmc.l2transport.content.tile.base.AbstractNodeBlockEntity;
import dev.xkmc.l2transport.content.tile.base.IRenderableNode;
import dev.xkmc.l2transport.content.tile.client.TooltipBuilder;
import dev.xkmc.l2transport.content.upgrades.Upgrade;
import dev.xkmc.l2transport.content.upgrades.UpgradeFlag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SerialClass
public abstract class AbstractFluxNodeBlockEntity<BE extends AbstractFluxNodeBlockEntity<BE>> extends AbstractNodeBlockEntity<BE>
		implements IGenericNodeBlockEntity, IRenderableNode {

	protected final NodalGenericHandler genericHandler = new NodalGenericHandler(this);

	@SerialClass.SerialField(toClient = true)
	public ResourceLocation capType;

	@SerialClass.SerialField
	private long dirtyTime;

	public AbstractFluxNodeBlockEntity(BlockEntityType<BE> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		flags.add(UpgradeFlag.THROUGH_PUT);
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
		return GenericCapabilityRegistry.getOrDefault(capType);
	}

	@Override
	public void setType(ICapabilityEntry<?> type, long time) {
		this.capType = type.id();
		dirtyTime = time;
		sync();
		for (BlockPos targets : getConnector().getAvailableTarget()) {
			if (level.getBlockEntity(targets) instanceof IGenericNodeBlockEntity node) {
				ICapabilityEntry<?> t = node.getCapType();
				if (t != type || time != node.getLastTypeTime()) {
					node.setType(type, time);
				}
			}
		}
	}

	@Override
	public long getLastTypeTime() {
		return dirtyTime;
	}

	@Override
	public void tick() {
		if (level != null && !level.isClientSide()) {
			long maxCapTime = dirtyTime;
			ICapabilityEntry<?> type = getCapType();
			for (BlockPos targets : getConnector().getAvailableTarget()) {
				if (level.getBlockEntity(targets) instanceof IGenericNodeBlockEntity node) {
					ICapabilityEntry<?> t = node.getCapType();
					if (t != type && node.getLastTypeTime() > maxCapTime) {
						maxCapTime = node.getLastTypeTime();
						type = t;
					}
				}
			}
			if (type != getCapType()) {
				setType(type, maxCapTime);
			}
		}
		super.tick();
	}

	@Override
	public int getMaxTransfer() {
		int cd = getCapType().getDefaultMax();
		for (Upgrade u : getUpgrades()) {
			cd = u.getMaxTransfer(cd);
		}
		return cd;
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

}
