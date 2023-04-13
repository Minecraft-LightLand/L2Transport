package dev.xkmc.lasertransport.content.items.select;

import dev.xkmc.lasertransport.init.registrate.LTBlocks;
import dev.xkmc.lasertransport.init.registrate.LTItems;

public class SelectorRegistry {

	public static final IItemSelector WAND = new ItemSelector(
			LTItems.LINKER.asStack(),
			LTItems.VALIDATOR.asStack(),
			LTItems.CLEAR.asStack(),
			LTItems.ROTATE.asStack(),
			LTItems.CONFIG.asStack()
	);

	public static final IItemSelector UPGRADE_BASIC = new ItemSelector(
			LTItems.VALVE_UP.asStack(),
			LTItems.WATCH_UP.asStack(),
			LTItems.SPEED_UP_0.asStack(),
			LTItems.DIST_UP_0.asStack(),
			LTItems.THR_UP_0.asStack(),
			LTItems.DROP_UP.asStack(),
			LTItems.PLACE_UP.asStack()
	);

	public static final IItemSelector FLUX = new FluxSelector();

	public static final IItemSelector NODE_ITEM = new ItemSelector(
			LTBlocks.B_ITEM_RETRIEVE.asStack(),
			LTBlocks.B_ITEM_SIMPLE.asStack(),
			LTBlocks.B_ITEM_ORDERED.asStack(),
			LTBlocks.B_ITEM_DISTRIBUTE.asStack(),
			LTBlocks.B_ITEM_SYNCED.asStack()
	);

	public static final IItemSelector NODE_FLUID = new ItemSelector(
			LTBlocks.B_FLUID_RETRIEVE.asStack(),
			LTBlocks.B_FLUID_SIMPLE.asStack(),
			LTBlocks.B_FLUID_ORDERED.asStack(),
			LTBlocks.B_FLUID_DISTRIBUTE.asStack(),
			LTBlocks.B_FLUID_SYNCED.asStack()
	);

	public static final IItemSelector NODE_FLUX = new ItemSelector(
			LTBlocks.B_FLUX_RETRIEVE.asStack(),
			LTBlocks.B_FLUX_SIMPLE.asStack(),
			LTBlocks.B_FLUX_ORDERED.asStack()
	);

	public static final IItemSelector CRAFT = new ItemSelector(
			LTBlocks.B_CRAFT_SIDE.asStack(),
			LTBlocks.B_CRAFT_CORE.asStack()
	);

	public static void register() {

	}

}
