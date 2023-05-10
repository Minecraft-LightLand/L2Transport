package dev.xkmc.lasertransport.content.craft.block;

import dev.xkmc.l2modularblock.mult.NeighborUpdateBlockMethod;
import dev.xkmc.lasertransport.content.craft.tile.CraftCoreBlockEntity;
import dev.xkmc.lasertransport.content.craft.tile.CraftSideBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CraftRestructureDetector implements NeighborUpdateBlockMethod {

	@Override
	public void neighborChanged(Block block, BlockState blockState, Level level, BlockPos blockPos, Block block1, BlockPos blockPos1, boolean b) {
		BlockEntity be = level.getBlockEntity(blockPos);
		if (be instanceof CraftSideBlockEntity side) {
			BlockPos pos = side.target.t();
			if (pos == null) return;
			be = level.getBlockEntity(pos);
		}
		if (be instanceof CraftCoreBlockEntity core) {
			core.markRevalidate();
		}
	}

}
