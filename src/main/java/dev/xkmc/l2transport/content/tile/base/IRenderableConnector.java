package dev.xkmc.l2transport.content.tile.base;

import net.minecraft.core.BlockPos;

import java.util.List;

public interface IRenderableConnector {

	List<BlockPos> getConnected();

	int getCoolDown(BlockPos target);

	int getMaxCoolDown(BlockPos target);

	CoolDownType getType(BlockPos target);

}
