package dev.xkmc.lasertransport.content.client.node;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.lasertransport.content.items.tools.ILinker;
import dev.xkmc.lasertransport.content.tile.base.IRenderableNode;
import dev.xkmc.lasertransport.init.data.LTModConfig;

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
