package dev.xkmc.l2transport.init.registrate;

import dev.xkmc.l2library.repack.registrate.util.entry.MenuEntry;
import dev.xkmc.l2transport.content.menu.filter.FluidConfigMenu;
import dev.xkmc.l2transport.content.menu.filter.FluidConfigScreen;
import dev.xkmc.l2transport.content.menu.filter.ItemConfigMenu;
import dev.xkmc.l2transport.content.menu.filter.ItemConfigScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ForgeRegistries;

import static dev.xkmc.l2transport.init.L2Transport.REGISTRATE;

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
