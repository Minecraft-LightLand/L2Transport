package dev.xkmc.l2transport.init.registrate;

import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.xkmc.l2transport.content.tools.*;
import dev.xkmc.l2transport.content.upgrades.*;
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
	public static final ItemEntry<UpgradeItem> VALVE_UP, WATCH_UP, DROP_UP, PLACE_UP,
			SPEED_UP_0, SPEED_UP_1, SPEED_UP_2, DIST_UP_0, THR_UP_0, THR_UP_1, THR_UP_2;

	static {
		LINKER = REGISTRATE.item("linker", LinkerItem::new).defaultModel().defaultLang().register();
		ROTATE = REGISTRATE.item("rotate", RotateItem::new).defaultModel().defaultLang().register();
		VALIDATOR = REGISTRATE.item("validator", ValidatorItem::new).defaultModel().defaultLang().register();
		CLEAR = REGISTRATE.item("clear", ClearItem::new).defaultModel().defaultLang().register();
		FLUX = REGISTRATE.item("flux_filter", FluxFilter::new).defaultModel().defaultLang().register();

		{
			VALVE_UP = REGISTRATE.item("valve_upgrade", p -> new UpgradeItem(p, new ValveUpgrade())).register();
			WATCH_UP = REGISTRATE.item("watch_upgrade", p -> new UpgradeItem(p, new WatchUpgrade())).register();
			DROP_UP = REGISTRATE.item("drop_upgrade", p -> new UpgradeItem(p, new LevelDropUpgrade())).register();
			PLACE_UP = REGISTRATE.item("place_upgrade", p -> new UpgradeItem(p, new LevelPlaceUpgrade())).register();
			SPEED_UP_0 = REGISTRATE.item("speed_upgrade_0", p -> new UpgradeItem(p, new SpeedUpgrade(2))).register();
			SPEED_UP_1 = REGISTRATE.item("speed_upgrade_1", p -> new UpgradeItem(p, new SpeedUpgrade(4))).register();
			SPEED_UP_2 = REGISTRATE.item("speed_upgrade_2", p -> new UpgradeItem(p, new SpeedUpgrade(8))).register();
			DIST_UP_0 = REGISTRATE.item("distance_upgrade_0", p -> new UpgradeItem(p, new DistanceUpgrade(2))).register();
			THR_UP_0 = REGISTRATE.item("throughput_upgrade_0", p -> new UpgradeItem(p, new ThroughputUpgrade(4))).register();
			THR_UP_1 = REGISTRATE.item("throughput_upgrade_1", p -> new UpgradeItem(p, new ThroughputUpgrade(16))).register();
			THR_UP_2 = REGISTRATE.item("throughput_upgrade_2", p -> new UpgradeItem(p, new ThroughputUpgrade(64))).register();

		}
	}


	public static void register() {
	}

	public static ItemEntry<Item> simpleItem(String id) {
		return REGISTRATE.item(id, Item::new).defaultModel().defaultLang().register();
	}

}
