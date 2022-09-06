package dev.xkmc.l2transport.init.registrate;

import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.xkmc.l2transport.content.tools.*;
import dev.xkmc.l2transport.init.L2Transport;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

import static dev.xkmc.l2transport.init.L2Transport.REGISTRATE;

@SuppressWarnings({"rawtypes", "unsafe"})
public class LTItems {

	public static class Tab extends CreativeModeTab {

		private final Supplier<ItemEntry> icon;

		public Tab(String id, Supplier<ItemEntry> icon) {
			super(L2Transport.MODID + "." + id);
			this.icon = icon;
		}

		@Override
		public ItemStack makeIcon() {
			return icon.get().asStack();
		}
	}

	public static final Tab TAB_MAIN = new Tab("transport", () -> LTItems.LINKER);

	static {
		REGISTRATE.creativeModeTab(() -> TAB_MAIN);
	}

	// -------- common --------
	public static final ItemEntry<LinkerItem> LINKER;
	public static final ItemEntry<RotateItem> ROTATE;
	public static final ItemEntry<ValidatorItem> VALIDATOR;
	public static final ItemEntry<ClearItem> CLEAR;
	public static final ItemEntry<FluxFilter> FLUX;

	static {
		LINKER = REGISTRATE.item("linker", LinkerItem::new).defaultModel().defaultLang().register();
		ROTATE = REGISTRATE.item("rotate", RotateItem::new).defaultModel().defaultLang().register();
		VALIDATOR = REGISTRATE.item("validator", ValidatorItem::new).defaultModel().defaultLang().register();
		CLEAR = REGISTRATE.item("clear", ClearItem::new).defaultModel().defaultLang().register();
		FLUX = REGISTRATE.item("flux_filter", FluxFilter::new).defaultModel().defaultLang().register();
	}


	public static void register() {
	}

	public static ItemEntry<Item> simpleItem(String id) {
		return REGISTRATE.item(id, Item::new).defaultModel().defaultLang().register();
	}

}
