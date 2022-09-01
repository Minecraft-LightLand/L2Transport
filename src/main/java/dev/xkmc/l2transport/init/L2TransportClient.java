package dev.xkmc.l2transport.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class L2TransportClient {

	public static void onCtorClient(IEventBus bus, IEventBus eventBus) {
		bus.addListener(L2TransportClient::clientSetup);
		bus.addListener(L2TransportClient::registerOverlays);
		bus.addListener(L2TransportClient::registerKeys);
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
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerKeys(RegisterKeyMappingsEvent event) {
	}
}
