package dev.xkmc.l2transport.content.tile.base;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.tile.client.TooltipBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@SerialClass
public class SidedBlockEntity extends BlockEntity
		implements IRenderableNode, IRenderableConnector {

	public SidedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public @NotNull <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
		if (level != null) {
			Direction dire = getBlockState().getValue(BlockStateProperties.FACING);
			BlockPos pos = getBlockPos().relative(dire);
			BlockEntity be = level.getBlockEntity(pos);
			if (be != null) {
				if (be instanceof SidedBlockEntity target) {
					Set<BlockPos> set = new TreeSet<>();
					set.add(getBlockPos());
					set.add(pos);
					return target.recursiveCap(cap, set);
				}
				return be.getCapability(cap, dire.getOpposite());
			}
		}
		return LazyOptional.empty();
	}

	private <C> LazyOptional<C> recursiveCap(Capability<C> cap, Set<BlockPos> set) {
		if (level != null) {
			Direction dire = getBlockState().getValue(BlockStateProperties.FACING);
			BlockPos pos = getBlockPos().relative(dire);
			if (set.contains(pos)) return LazyOptional.empty();
			BlockEntity be = level.getBlockEntity(pos);
			if (be != null) {
				if (be instanceof SidedBlockEntity target) {
					set.add(pos);
					return target.recursiveCap(cap, set);
				}
				return be.getCapability(cap, dire.getOpposite());
			}
		}
		return LazyOptional.empty();
	}

	@Override
	public IRenderableConnector getConnector() {
		return this;
	}

	@Override
	public TooltipBuilder getTooltips() {
		return new TooltipBuilder();
	}

	@Override
	public List<BlockPos> getVisibleConnection() {
		return List.of(getBlockPos().relative(getBlockState().getValue(BlockStateProperties.FACING)));
	}

	@Override
	public int getCoolDown(BlockPos target) {
		return 0;
	}

	@Override
	public int getMaxCoolDown(BlockPos target) {
		return 1;
	}

	@Override
	public CoolDownType getType(BlockPos target) {
		return CoolDownType.GREEN;
	}

}
