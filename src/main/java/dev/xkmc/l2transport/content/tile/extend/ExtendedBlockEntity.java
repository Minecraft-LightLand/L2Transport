package dev.xkmc.l2transport.content.tile.extend;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.tile.base.ConnectionRenderBlockEntity;
import dev.xkmc.l2transport.content.tile.base.ILinkableNode;
import dev.xkmc.l2transport.content.tile.client.TooltipBuilder;
import dev.xkmc.l2transport.content.tile.client.TooltipType;
import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class ExtendedBlockEntity extends ConnectionRenderBlockEntity
		implements IExtendedBlockEntity, ILinkableNode {

	@Nullable
	@SerialClass.SerialField(toClient = true)
	public BlockPos target = null;

	public ExtendedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public @NotNull <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
		return IExtendedBlockEntity.getCapabilityImpl(this, cap);
	}

	@Override
	public <C> LazyOptional<C> getCapabilityOneStep(Capability<C> cap) {
		if (level != null && target != null) {
			BlockEntity be = level.getBlockEntity(target);
			if (be != null) {
				if (be instanceof SidedBlockEntity) {
					return LazyOptional.empty();
				}
				//FIXME fix direction
				return be.getCapability(cap);
			}
		}
		return LazyOptional.empty();
	}

	@Nullable
	@Override
	public BlockPos getTarget() {
		return target;
	}

	@Override
	public boolean isTargetValid(BlockPos pos) {
		return IExtendedBlockEntity.super.isTargetValid(pos);
	}

	@Override
	public void link(BlockPos pos) {
		if (pos.equals(target) || pos.equals(getBlockPos())) {
			target = null;
		} else {
			target = pos;
		}
		sync();
	}

	@Override
	public void validate() {
		if (target != null && !isTargetValid(target)) {
			target = null;
		}
		sync();
	}

	@Override
	public void removeAll() {
		target = null;
		sync();
	}

	@Override
	public int getMaxDistanceSqr() {
		return 256;//TODO configurable
	}

	@Override
	public TooltipBuilder getTooltips() {
		var ans = new TooltipBuilder();
		if (target != null && !isTargetValid(target)) {
			ans.add(TooltipType.DESC, LangData.INVALID.get());
		}
		return ans;
	}

}
