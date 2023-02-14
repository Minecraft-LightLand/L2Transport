package dev.xkmc.lasertransport.events;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.lasertransport.content.client.overlay.NumberSetOverlay;
import dev.xkmc.lasertransport.content.client.overlay.ToolSelectionOverlay;
import dev.xkmc.lasertransport.content.configurables.NumericAdjustor;
import dev.xkmc.lasertransport.content.items.select.IItemSelector;
import dev.xkmc.lasertransport.init.data.Keys;
import dev.xkmc.lasertransport.init.data.LTModConfig;
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
			IItemSelector sel = IItemSelector.getSelection(Proxy.getClientPlayer());
			if (sel == null) return;
			if (event.getKey() == Keys.UP.map.getKey().getValue() && event.getAction() == 1) {
				sel.move(-1);
			} else if (event.getKey() == Keys.DOWN.map.getKey().getValue() && event.getAction() == 1) {
				sel.move(1);
			}
		}
	}

	private static double scroll;

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void scrollEvent(InputEvent.MouseScrollingEvent event) {
		double scroll_tick = LTModConfig.CLIENT.scrollTick.get();
		double delta = event.getScrollDelta();
		scroll += delta;
		int diff = 0;
		if (scroll > scroll_tick) {
			diff = (int) Math.floor(scroll / scroll_tick);
			scroll -= diff * scroll_tick;
		} else if (scroll < -scroll_tick) {
			diff = -(int) Math.floor(-scroll / scroll_tick);
			scroll -= diff * scroll_tick;
		}
		if (NumberSetOverlay.isScreenOn()) {
			while (diff > 0) {
				NumberSetOverlay.up();
				diff--;
			}
			while (diff < 0) {
				NumberSetOverlay.down();
				diff++;
			}
			event.setCanceled(true);
		} else if (ToolSelectionOverlay.INSTANCE.isScreenOn() &&
				(!LTModConfig.CLIENT.selectionScrollRequireShift.get() ||
						Proxy.getClientPlayer().isShiftKeyDown())) {
			IItemSelector sel = IItemSelector.getSelection(Proxy.getClientPlayer());
			if (sel == null) return;
			sel.move(diff);
			event.setCanceled(true);
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
