package dev.xkmc.lasertransport.content.tile.flux;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lasertransport.content.capability.generic.GenericCapabilityRegistry;
import dev.xkmc.lasertransport.content.capability.generic.ICapabilityEntry;
import dev.xkmc.lasertransport.content.capability.generic.IGenericNodeBlockEntity;
import dev.xkmc.lasertransport.content.capability.generic.NodalGenericHandler;
import dev.xkmc.lasertransport.content.client.overlay.TooltipBuilder;
import dev.xkmc.lasertransport.content.client.overlay.TooltipType;
import dev.xkmc.lasertransport.content.items.upgrades.UpgradeFlag;
import dev.xkmc.lasertransport.content.tile.base.AbstractNodeBlockEntity;
import dev.xkmc.lasertransport.content.tile.base.IRenderableNode;
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
	public TooltipBuilder getTooltips() {
		TooltipBuilder ans = super.getTooltips();
		ans.add(TooltipType.NAME, getCapType().getTypeDesc());
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
