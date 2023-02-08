package dev.xkmc.l2transport.events;

import dev.xkmc.l2transport.content.tile.client.overlay.ToolSelection;
import dev.xkmc.l2transport.content.tools.ToolSelectionHelper;
import dev.xkmc.l2transport.init.data.Keys;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientGeneralEvents {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void keyEvent(InputEvent.Key event) {
		if (ToolSelection.INSTANCE.isScreenOn()) {
			if (event.getKey() == Keys.UP.map.getKey().getValue() && event.getAction() == 1) {
				ToolSelectionHelper.move(-1);
			} else if (event.getKey() == Keys.DOWN.map.getKey().getValue() && event.getAction() == 1) {
				ToolSelectionHelper.move(1);
			}
		}

	}

}
