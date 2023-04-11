package dev.xkmc.lasertransport.content.craft.block;

import dev.xkmc.l2library.block.mult.OnReplacedBlockMethod;
import dev.xkmc.lasertransport.content.craft.tile.CraftCoreBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RemoveDestroyMethod implements OnReplacedBlockMethod {

	@Override
	public void onReplaced(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
		if (blockState.getBlock() != blockState1.getBlock() && level.getBlockEntity(blockPos) instanceof CraftCoreBlockEntity be) {
			be.removeConnected();
		}
	}

}
