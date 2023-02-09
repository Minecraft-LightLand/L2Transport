package dev.xkmc.l2transport.events;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2transport.content.client.overlay.ToolSelectionOverlay;
import dev.xkmc.l2transport.content.items.select.ItemSelector;
import dev.xkmc.l2transport.init.data.Keys;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientGeneralEvents {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void keyEvent(InputEvent.Key event) {
		if (ToolSelectionOverlay.INSTANCE.isScreenOn()) {
			ItemSelector sel = ItemSelector.getSelection(Proxy.getClientPlayer());
			if (sel == null) return;
			if (event.getKey() == Keys.UP.map.getKey().getValue() && event.getAction() == 1) {
				sel.move(-1);
			} else if (event.getKey() == Keys.DOWN.map.getKey().getValue() && event.getAction() == 1) {
				sel.move(1);
			}
		}

	}

}
