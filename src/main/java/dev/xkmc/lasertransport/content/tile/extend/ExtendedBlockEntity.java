package dev.xkmc.lasertransport.content.tile.extend;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lasertransport.content.capability.wrapper.ForgeCapabilityHolder;
import dev.xkmc.lasertransport.content.capability.wrapper.ICapabilityHolder;
import dev.xkmc.lasertransport.content.capability.wrapper.IFakeCapabilityTile;
import dev.xkmc.lasertransport.content.client.overlay.TooltipBuilder;
import dev.xkmc.lasertransport.content.client.overlay.TooltipType;
import dev.xkmc.lasertransport.content.tile.base.ConnectionRenderBlockEntity;
import dev.xkmc.lasertransport.content.tile.base.ILinkableNode;
import dev.xkmc.lasertransport.init.data.LTModConfig;
import dev.xkmc.lasertransport.init.data.LangData;
import dev.xkmc.lasertransport.util.Holder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class ExtendedBlockEntity extends ConnectionRenderBlockEntity
		implements IExtendedBlockEntity, ILinkableNode, IFakeCapabilityTile {

	@SerialClass.SerialField(toClient = true)
	private Holder target = new Holder(null);

	public ExtendedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public @NotNull <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
		return IExtendedBlockEntity.getCapabilityImpl(this, new ForgeCapabilityHolder<>(cap));
	}

	@Override
	public <C> LazyOptional<C> getCapability(@NotNull ICapabilityHolder<C> cap) {
		return IExtendedBlockEntity.getCapabilityImpl(this, cap);
	}

	@Override
	public <C> LazyOptional<C> getCapabilityOneStep(ICapabilityHolder<C> cap) {
		if (level != null && getTarget() != null) {
			BlockEntity be = level.getBlockEntity(getTarget());
			if (be != null) {
				return cap.getHolder(this, getTarget());
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
	public @Nullable Level getTargetLevel() {
		return getLevel();
	}

	@Override
	public boolean isTargetValid(BlockPos pos) {
		return getLevel() != null && getLevel().getBlockEntity(pos) != null;
	}

	@Override
	public LangData link(BlockPos pos, Level level) {
		if (level != this.level) {
			return LangData.MSG_LINKER_CANCEL;
		}
		if (pos.equals(getTarget()) || pos.equals(getBlockPos())) {
			target = new Holder(null);
			sync();
			return LangData.MSG_LINKER_REMOVE;
		} else {
			target = new Holder(pos.immutable());
			sync();
			return LangData.MSG_LINKER_SUCCEED;
		}
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
