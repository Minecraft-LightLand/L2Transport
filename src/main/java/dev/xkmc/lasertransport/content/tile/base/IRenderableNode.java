package dev.xkmc.lasertransport.content.tile.base;

import net.minecraft.core.BlockPos;

public interface IRenderableNode extends ITooltipNode {

	IRenderableConnector getConnector();

	boolean isTargetValid(BlockPos pos);

}
