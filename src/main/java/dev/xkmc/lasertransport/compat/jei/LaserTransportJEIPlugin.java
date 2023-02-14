package dev.xkmc.lasertransport.compat.jei;

import dev.xkmc.lasertransport.content.menu.filter.FluidConfigScreen;
import dev.xkmc.lasertransport.content.menu.filter.ItemConfigScreen;
import dev.xkmc.lasertransport.init.LaserTransport;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class LaserTransportJEIPlugin implements IModPlugin {

	public static final ResourceLocation ID = new ResourceLocation(LaserTransport.MODID, "main");

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addGhostIngredientHandler(ItemConfigScreen.class, new ItemFilterHandler());
		registration.addGhostIngredientHandler(FluidConfigScreen.class, new FluidFilterHandler());
	}

}
