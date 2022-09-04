package dev.xkmc.l2transport.content.tile.extend;

import dev.xkmc.l2library.serial.SerialClass;
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

@SerialClass
public class SidedBlockEntity extends BlockEntity
		implements IExtendedBlockEntity {

	public SidedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public @NotNull <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
		return IExtendedBlockEntity.getCapabilityImpl(this, cap);
	}

	@Override
	public <C> LazyOptional<C> getCapabilityOneStep(Capability<C> cap) {
		if (level != null) {
			Direction dire = getBlockState().getValue(BlockStateProperties.FACING);
			BlockPos pos = getBlockPos().relative(dire);
			BlockEntity be = level.getBlockEntity(pos);
			if (be != null) {
				if (be instanceof SidedBlockEntity) {
					return LazyOptional.empty();
				}
				return be.getCapability(cap, dire.getOpposite());
			}
		}
		return LazyOptional.empty();
	}

	@Override
	public @Nullable BlockPos getTarget() {
		return getBlockPos().relative(getBlockState().getValue(BlockStateProperties.FACING));
	}

}
