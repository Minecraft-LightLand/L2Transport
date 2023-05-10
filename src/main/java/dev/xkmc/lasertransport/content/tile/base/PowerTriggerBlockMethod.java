package dev.xkmc.lasertransport.content.tile.base;

import dev.xkmc.l2modularblock.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2modularblock.mult.DefaultStateBlockMethod;
import dev.xkmc.l2modularblock.mult.ScheduleTickBlockMethod;
import dev.xkmc.l2modularblock.one.BlockPowerBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class PowerTriggerBlockMethod implements CreateBlockStateBlockMethod, BlockPowerBlockMethod, DefaultStateBlockMethod, ScheduleTickBlockMethod {

	public PowerTriggerBlockMethod() {
	}

	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.TRIGGERED);
	}

	public int getSignal(BlockState bs, BlockGetter r, BlockPos pos, Direction d) {
		return bs.getValue(BlockStateProperties.TRIGGERED) ? 15 : 0;
	}

	public BlockState getDefaultState(BlockState state) {
		return state.setValue(BlockStateProperties.TRIGGERED, false);
	}

	@Override
	public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
		serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(BlockStateProperties.TRIGGERED, false));
	}

}
