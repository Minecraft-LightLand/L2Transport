package dev.xkmc.l2transport.content.tile.extend;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.tile.base.ConnectionRenderBlockEntity;
import dev.xkmc.l2transport.content.tile.base.ILinkableNode;
import dev.xkmc.l2transport.content.client.overlay.TooltipBuilder;
import dev.xkmc.l2transport.content.client.overlay.TooltipType;
import dev.xkmc.l2transport.init.data.LTModConfig;
import dev.xkmc.l2transport.init.data.LangData;
import dev.xkmc.l2transport.util.Holder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class ExtendedBlockEntity extends ConnectionRenderBlockEntity
		implements IExtendedBlockEntity, ILinkableNode {

	@SerialClass.SerialField(toClient = true)
	private Holder target = new Holder(null);

	public ExtendedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public @NotNull <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
		return IExtendedBlockEntity.getCapabilityImpl(this, cap);
	}

	@Override
	public <C> LazyOptional<C> getCapabilityOneStep(Capability<C> cap) {
		if (level != null && getTarget() != null) {
			BlockEntity be = level.getBlockEntity(getTarget());
			if (be != null) {
				return getCapability(cap, getTarget());
			}
		}
		return LazyOptional.empty();
	}

	@Nullable
	@Override
	public BlockPos getTarget() {
		return target.t();
	}

	@Override
	public boolean isTargetValid(BlockPos pos) {
		return IExtendedBlockEntity.super.isTargetValid(pos);
	}

	@Override
	public void link(BlockPos pos) {
		if (pos.equals(getTarget()) || pos.equals(getBlockPos())) {
			target = new Holder(null);
		} else {
			target = new Holder(pos.immutable());
		}
		sync();
	}

	@Override
	public void validate() {
		if (getTarget() != null && !isTargetValid(getTarget())) {
			target = new Holder(null);
		}
		sync();
	}

	@Override
	public void removeAll() {
		target = new Holder(null);
		sync();
	}

	@Override
	public int getMaxDistanceSqr() {
		int cd = LTModConfig.COMMON.defaultNodeDistance.get();
		return cd * cd;
	}

	@Override
	public TooltipBuilder getTooltips() {
		var ans = new TooltipBuilder();
		ans.add(TooltipType.NAME, Component.translatable(getBlockState().getBlock().getDescriptionId()).withStyle(ChatFormatting.YELLOW));
		ans.add(TooltipType.DESC, LangData.EXTENDED.get());
		if (getTarget() != null && !isTargetValid(getTarget())) {
			ans.add(TooltipType.DESC, LangData.INVALID.get());
		}
		return ans;
	}

}
