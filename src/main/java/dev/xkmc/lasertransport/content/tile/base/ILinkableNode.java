package dev.xkmc.lasertransport.content.tile.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface ILinkableNode {

	void link(BlockPos clickedPos, Level level);

	void validate();

	void removeAll();

	int getMaxDistanceSqr();

	boolean isTargetValid(BlockPos pos);

	default boolean crossDimension() {
		return false;
	}

}
