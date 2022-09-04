package dev.xkmc.l2transport.content.tile.base;

import dev.xkmc.l2transport.content.connector.Connector;
import dev.xkmc.l2transport.content.tile.client.TooltipBuilder;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public interface IRenderableNode {

	Connector getConnector();

	TooltipBuilder getTooltips();

}
