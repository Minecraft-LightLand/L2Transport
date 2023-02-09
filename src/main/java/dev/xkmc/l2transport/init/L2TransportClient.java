package dev.xkmc.l2transport.init;

import dev.xkmc.l2transport.content.client.overlay.OverlayRenderer;
import dev.xkmc.l2transport.content.client.overlay.ToolSelection;
import dev.xkmc.l2transport.events.ClientGeneralEvents;
import dev.xkmc.l2transport.init.data.Keys;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class L2TransportClient {

	public static void onCtorClient(IEventBus bus, IEventBus eventBus) {
		bus.addListener(L2TransportClient::clientSetup);
		bus.addListener(L2TransportClient::registerOverlays);
		bus.addListener(L2TransportClient::registerKeys);
		eventBus.register(ClientGeneralEvents.class);
	}

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		registerItemProperties();
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerItemProperties() {
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerOverlays(RegisterGuiOverlaysEvent event) {
		event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "node_info", new OverlayRenderer());
		event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "tool_select", ToolSelection.INSTANCE);
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerKeys(RegisterKeyMappingsEvent event) {
		for (Keys key : Keys.values()) {
			event.register(key.map);
		}
	}
}
