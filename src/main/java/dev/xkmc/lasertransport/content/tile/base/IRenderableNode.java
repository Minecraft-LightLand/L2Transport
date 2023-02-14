package dev.xkmc.lasertransport.content.tile.base;

import dev.xkmc.lasertransport.content.client.overlay.TooltipBuilder;
import net.minecraft.core.BlockPos;

public interface IRenderableNode {

	IRenderableConnector getConnector();

	TooltipBuilder getTooltips();

	boolean isTargetValid(BlockPos pos);

}
