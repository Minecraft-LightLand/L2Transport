package dev.xkmc.lasertransport.init.registrate;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.lasertransport.content.menu.filter.FluidConfigMenu;
import dev.xkmc.lasertransport.content.menu.filter.FluidConfigScreen;
import dev.xkmc.lasertransport.content.menu.filter.ItemConfigMenu;
import dev.xkmc.lasertransport.content.menu.filter.ItemConfigScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ForgeRegistries;

import static dev.xkmc.lasertransport.init.LaserTransport.REGISTRATE;

public class LTMenus {

	public static final MenuEntry<ItemConfigMenu> MT_ITEM = REGISTRATE.menu("item_config",
					ItemConfigMenu::fromNetwork,
					() -> ItemConfigScreen::new)
			.lang(LTMenus::getLangKey).register();

	public static final MenuEntry<FluidConfigMenu> MT_FLUID = REGISTRATE.menu("fluid_config",
					FluidConfigMenu::fromNetwork,
					() -> FluidConfigScreen::new)
			.lang(LTMenus::getLangKey).register();

	public static String getLangKey(MenuType<?> menu) {
		ResourceLocation rl = ForgeRegistries.MENU_TYPES.getKey(menu);
		assert rl != null;
		return "container." + rl.getNamespace() + "." + rl.getPath();
	}

	public static void register() {

	}


}
