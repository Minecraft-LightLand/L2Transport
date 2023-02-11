package dev.xkmc.l2transport.compat.jei;

import dev.xkmc.l2transport.content.menu.ItemConfigScreen;
import dev.xkmc.l2transport.init.L2Transport;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class L2TransportJEIPlugin implements IModPlugin {

	public static final ResourceLocation ID = new ResourceLocation(L2Transport.MODID, "main");

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addGhostIngredientHandler(ItemConfigScreen.class, new ItemFilterHandler());
	}

}
