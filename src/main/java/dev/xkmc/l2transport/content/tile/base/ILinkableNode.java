package dev.xkmc.l2transport.content.tile.base;

import net.minecraft.core.BlockPos;

public interface ILinkableNode {

	void link(BlockPos clickedPos);

	void validate();

	void removeAll();

	int getMaxDistanceSqr();

	boolean isTargetValid(BlockPos pos);

}
