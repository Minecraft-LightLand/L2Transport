package dev.xkmc.l2transport.content.tile.base;

import dev.xkmc.l2transport.content.tile.client.TooltipBuilder;

public interface IRenderableNode {

	IRenderableConnector getConnector();

	TooltipBuilder getTooltips();

}
