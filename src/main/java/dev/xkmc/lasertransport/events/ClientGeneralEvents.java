package dev.xkmc.lasertransport.events;

import dev.xkmc.lasertransport.content.configurables.NumericAdjustor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientGeneralEvents {

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void tickEvent(TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		if (Minecraft.getInstance().player == null) return;
		NumericAdjustor.tick();
	}

}
