package dev.xkmc.l2transport.events;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2transport.content.client.overlay.NumberSetOverlay;
import dev.xkmc.l2transport.content.client.overlay.ToolSelectionOverlay;
import dev.xkmc.l2transport.content.configurables.NumericAdjustor;
import dev.xkmc.l2transport.content.items.select.ItemSelector;
import dev.xkmc.l2transport.init.data.Keys;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientGeneralEvents {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void keyEvent(InputEvent.Key event) {
		if (NumberSetOverlay.isScreenOn()) {
			if (event.getKey() == Keys.UP.map.getKey().getValue() && event.getAction() == 1) {
				NumberSetOverlay.up();
			} else if (event.getKey() == Keys.DOWN.map.getKey().getValue() && event.getAction() == 1) {
				NumberSetOverlay.down();
			} else if (event.getKey() == Keys.LEFT.map.getKey().getValue() && event.getAction() == 1) {
				NumberSetOverlay.left();
			} else if (event.getKey() == Keys.RIGHT.map.getKey().getValue() && event.getAction() == 1) {
				NumberSetOverlay.right();
			}
		} else if (ToolSelectionOverlay.INSTANCE.isScreenOn()) {
			ItemSelector sel = ItemSelector.getSelection(Proxy.getClientPlayer());
			if (sel == null) return;
			if (event.getKey() == Keys.UP.map.getKey().getValue() && event.getAction() == 1) {
				sel.move(-1);
			} else if (event.getKey() == Keys.DOWN.map.getKey().getValue() && event.getAction() == 1) {
				sel.move(1);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void tickEvent(TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		if (Minecraft.getInstance().player == null) return;
		NumericAdjustor.tick();
	}

}
