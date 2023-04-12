package dev.xkmc.lasertransport.content.tile.extend;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lasertransport.content.capability.wrapper.ForgeCapabilityHolder;
import dev.xkmc.lasertransport.content.capability.wrapper.ICapabilityHolder;
import dev.xkmc.lasertransport.content.capability.wrapper.IFakeCapabilityTile;
import dev.xkmc.lasertransport.content.client.overlay.TooltipBuilder;
import dev.xkmc.lasertransport.content.client.overlay.TooltipType;
import dev.xkmc.lasertransport.content.tile.base.ConnectionRenderBlockEntity;
import dev.xkmc.lasertransport.content.tile.base.ILinkableNode;
import dev.xkmc.lasertransport.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class EnderExtendedBlockEntity extends ConnectionRenderBlockEntity
		implements IExtendedBlockEntity, ILinkableNode, IFakeCapabilityTile {


	@SerialClass.SerialField(toClient = true)
	private MultiLevelTarget target = MultiLevelTarget.NULL;

	public EnderExtendedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
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
		return LazyOptional.empty();
	}

	@Nullable
	@Override
	public BlockPos getTarget() {
		return target == MultiLevelTarget.NULL ? null : target.pos();
	}

	@Override
	public @Nullable Level getTargetLevel() {
		if (level == null) return null;
		if (level.getServer() == null) return null;
		return target.getLevel(level);
	}

	@Override
	public void link(BlockPos clickedPos, Level level) {
		if (!(level.getBlockEntity(clickedPos) instanceof ExtendedBlockEntity)) {
			return;
		}
		target = MultiLevelTarget.of(level, clickedPos);
		sync();
	}

	@Override
	public void validate() {
		if (level == null || level.isClientSide()) return;
		if (target == MultiLevelTarget.NULL) return;
		Level targetLevel = target.getLevel(level);
		if (targetLevel != null) {
			if (targetLevel.getBlockEntity(target.pos()) instanceof ExtendedBlockEntity) {
				return;
			}
		}
		removeAll();
	}

	@Override
	public void removeAll() {
		target = MultiLevelTarget.NULL;
		sync();
	}

	@Override
	public boolean crossDimension() {
		return true;
	}

	@Override
	public int getMaxDistanceSqr() {
		return 0;
	}

	@Override
	public boolean isTargetValid(BlockPos pos) {
		return true;
	}

	@Override
	public TooltipBuilder getTooltips() {
		var ans = new TooltipBuilder();
		ans.add(TooltipType.NAME, Component.translatable(getBlockState().getBlock().getDescriptionId()).withStyle(ChatFormatting.YELLOW));
		ans.add(TooltipType.DESC, LangData.ENDER_EXTEND.get());
		if (getTarget() != null) {
			//ans.add(TooltipType.DESC, LangData.INVALID.get());
			ans.add(TooltipType.FILTER, Component.literal(target.dim() + ", " + target.pos()));
		} else {
			ans.add(TooltipType.FILTER, Component.literal("no target"));
		}
		return ans;
	}

}
