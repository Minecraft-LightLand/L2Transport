package dev.xkmc.l2transport.content.tile.base;

import net.minecraft.core.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

public interface ILinkableNode {

	void link(BlockPos clickedPos);

	void validate();

	void removeAll();

	int getMaxDistanceSqr();

	Capability<?> getValidTarget();
}
