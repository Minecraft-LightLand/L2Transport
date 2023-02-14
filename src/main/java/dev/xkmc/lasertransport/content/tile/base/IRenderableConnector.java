package dev.xkmc.lasertransport.content.tile.base;

import net.minecraft.core.BlockPos;

import java.util.List;

public interface IRenderableConnector {

	List<BlockPos> getVisibleConnection();

	int getCoolDown(BlockPos target);

	int getMaxCoolDown(BlockPos target);

	CoolDownType getType(BlockPos target);

}
