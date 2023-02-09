package dev.xkmc.l2transport.content.client.node;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2transport.content.items.tools.ILinker;
import dev.xkmc.l2transport.content.tile.base.IRenderableNode;
import dev.xkmc.l2transport.init.data.LTModConfig;

public class RenderManager {

	public static NodeRenderType getRenderConfig(IRenderableNode node) {
		boolean render = LTModConfig.CLIENT.renderLinks.get();
		if (render) {
			return NodeRenderType.ALL;
		}
		var player = Proxy.getClientPlayer();
		if (player.getMainHandItem().getItem() instanceof ILinker) {
			return NodeRenderType.ALL;
		}
		if (player.getOffhandItem().getItem() instanceof ILinker) {
			return NodeRenderType.ALL;
		}
		return NodeRenderType.FILTER;
	}

}
