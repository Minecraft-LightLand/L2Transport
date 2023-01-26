package dev.xkmc.l2transport.content.tile.base;

import dev.xkmc.l2library.block.TickableBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;
import dev.xkmc.l2transport.content.connector.IConnector;
import dev.xkmc.l2transport.content.tile.client.TooltipBuilder;
import dev.xkmc.l2transport.content.tile.client.TooltipType;
import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;

@SerialClass
public abstract class AbstractNodeBlockEntity<BE extends AbstractNodeBlockEntity<BE>> extends ConnectionRenderBlockEntity
		implements TickableBlockEntity, IRenderableNode, ILinkableNode, INodeBlockEntity {

	public AbstractNodeBlockEntity(BlockEntityType<BE> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	private boolean dirty = false;

	@Override
	public void tick() {
		if (level != null && !level.isClientSide && dirty) {
			getConnector().perform();
			sync();
			dirty = false;
		}
		getConnector().tick();
	}

	public int getMaxCoolDown() {
		return 80;//TODO configurable
	}

	public abstract IConnector getConnector();

	public final void refreshCoolDown(BlockPos target, boolean success, boolean simulate) {
		getConnector().refreshCoolDown(target, success, simulate);
		dirty = true;
	}

	// linkable

	@Override
	public int getMaxDistanceSqr() {
		return 256;//TODO configurable
	}

	@Override
	public void link(BlockPos pos) {
		if (pos.equals(getBlockPos()))
			return;
		getConnector().link(pos);
		sync();
	}

	@Override
	public void validate() {
		getConnector().removeIf(e -> !isTargetValid(e));
		sync();
	}

	@Override
	public void removeAll() {
		getConnector().removeIf(e -> true);
		sync();
	}

	// render related

	@Override
	public TooltipBuilder getTooltips() {
		var ans = new TooltipBuilder();
		ans.add(TooltipType.NAME, Component.translatable(getBlockState().getBlock().getDescriptionId()).withStyle(ChatFormatting.YELLOW));
		if (getConnector().getVisibleConnection().stream().anyMatch(e -> !isTargetValid(e))) {
			ans.add(TooltipType.DESC, LangData.INVALID.get());
		}
		return ans;
	}

	@Override
	public boolean isTargetValid(BlockPos pos) {
		return getCapability(getValidTarget(), pos).resolve().isPresent();
	}

	public abstract Capability<?> getValidTarget();

}
