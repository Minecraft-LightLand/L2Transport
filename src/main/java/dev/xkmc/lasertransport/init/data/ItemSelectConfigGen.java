package dev.xkmc.lasertransport.init.data;

import dev.xkmc.l2library.init.data.L2ConfigManager;
import dev.xkmc.l2library.init.events.select.item.SimpleItemSelectConfig;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.lasertransport.init.LaserTransport;
import dev.xkmc.lasertransport.init.registrate.LTBlocks;
import dev.xkmc.lasertransport.init.registrate.LTItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class ItemSelectConfigGen extends ConfigDataProvider {

	public ItemSelectConfigGen(DataGenerator generator) {
		super(generator, "data/" + LaserTransport.MODID + "/l2library_config/" +
				L2ConfigManager.ITEM_SELECTOR.getID() + "/", "Laser Transport Item Selector Config");
	}

	@Override
	public void add(Map<String, BaseConfig> map) {

		map.put("transport", new SimpleItemSelectConfig()
				.add(modLoc("wand"),
						LTItems.LINKER.get(),
						LTItems.VALIDATOR.get(),
						LTItems.CLEAR.get(),
						LTItems.ROTATE.get(),
						LTItems.CONFIG.get())
				.add(modLoc("upgrade_basic"),
						LTItems.VALVE_UP.get(),
						LTItems.WATCH_UP.get(),
						LTItems.SPEED_UP_0.get(),
						LTItems.DIST_UP_0.get(),
						LTItems.THR_UP_0.get(),
						LTItems.DROP_UP.get(),
						LTItems.PLACE_UP.get())
				.add(modLoc("node_item"),
						LTBlocks.B_ITEM_RETRIEVE.get().asItem(),
						LTBlocks.B_ITEM_SIMPLE.get().asItem(),
						LTBlocks.B_ITEM_ORDERED.get().asItem(),
						LTBlocks.B_ITEM_DISTRIBUTE.get().asItem(),
						LTBlocks.B_ITEM_SYNCED.get().asItem())
				.add(modLoc("node_fluid"),
						LTBlocks.B_FLUID_RETRIEVE.get().asItem(),
						LTBlocks.B_FLUID_SIMPLE.get().asItem(),
						LTBlocks.B_FLUID_ORDERED.get().asItem(),
						LTBlocks.B_FLUID_DISTRIBUTE.get().asItem(),
						LTBlocks.B_FLUID_SYNCED.get().asItem())
				.add(modLoc("node_flux"),
						LTBlocks.B_FLUX_RETRIEVE.get().asItem(),
						LTBlocks.B_FLUX_SIMPLE.get().asItem(),
						LTBlocks.B_FLUX_ORDERED.get().asItem(),
						LTBlocks.B_FLUX_DISTRIBUTE.get().asItem(),
						LTBlocks.B_FLUX_SYNCED.get().asItem())
				.add(modLoc("craft"),
						LTBlocks.B_CRAFT_SIDE.get().asItem(),
						LTBlocks.B_CRAFT_CORE.get().asItem())
		);

	}

	private static ResourceLocation modLoc(String id) {
		return new ResourceLocation(LaserTransport.MODID, id);
	}

}
