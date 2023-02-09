package dev.xkmc.l2transport.content.tile.base;

import dev.xkmc.l2transport.content.client.overlay.TooltipBuilder;
import net.minecraft.core.BlockPos;

public interface IRenderableNode {

	IRenderableConnector getConnector();

	TooltipBuilder getTooltips();

	boolean isTargetValid(BlockPos pos);

}
