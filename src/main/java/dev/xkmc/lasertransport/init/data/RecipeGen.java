package dev.xkmc.lasertransport.init.data;

import dev.xkmc.l2library.repack.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.repack.registrate.util.DataIngredient;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.lasertransport.init.LaserTransport;
import dev.xkmc.lasertransport.init.registrate.LTBlocks;
import dev.xkmc.lasertransport.init.registrate.LTItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.UpgradeRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiFunction;

public class RecipeGen {

	private static final String currentFolder = "";

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		full(pvd, Items.REDSTONE_BLOCK, Items.IRON_INGOT, Items.COPPER_INGOT, LTBlocks.B_EXTENDED.get().asItem(), 16);
		pvd.stonecutting(DataIngredient.items(LTBlocks.B_EXTENDED.get()), LTBlocks.B_ITEM_SIMPLE);
		pvd.stonecutting(DataIngredient.items(LTBlocks.B_EXTENDED.get()), LTBlocks.B_FLUID_SIMPLE);
		pvd.stonecutting(DataIngredient.items(LTBlocks.B_EXTENDED.get()), LTBlocks.B_FLUX_SIMPLE);
		pvd.stonecutting(DataIngredient.items(LTBlocks.B_ITEM_SIMPLE.get()), LTBlocks.B_ITEM_RETRIEVE);
		pvd.stonecutting(DataIngredient.items(LTBlocks.B_ITEM_SIMPLE.get()), LTBlocks.B_ITEM_ORDERED);
		pvd.stonecutting(DataIngredient.items(LTBlocks.B_ITEM_SIMPLE.get()), LTBlocks.B_ITEM_DISTRIBUTE);
		pvd.stonecutting(DataIngredient.items(LTBlocks.B_ITEM_SIMPLE.get()), LTBlocks.B_ITEM_SYNCED);
		pvd.stonecutting(DataIngredient.items(LTBlocks.B_FLUID_SIMPLE.get()), LTBlocks.B_FLUID_RETRIEVE);
		pvd.stonecutting(DataIngredient.items(LTBlocks.B_FLUID_SIMPLE.get()), LTBlocks.B_FLUID_ORDERED);
		pvd.stonecutting(DataIngredient.items(LTBlocks.B_FLUID_SIMPLE.get()), LTBlocks.B_FLUID_DISTRIBUTE);
		pvd.stonecutting(DataIngredient.items(LTBlocks.B_FLUID_SIMPLE.get()), LTBlocks.B_FLUID_SYNCED);
		pvd.stonecutting(DataIngredient.items(LTBlocks.B_FLUX_SIMPLE.get()), LTBlocks.B_FLUX_RETRIEVE);
		pvd.stonecutting(DataIngredient.items(LTBlocks.B_FLUX_SIMPLE.get()), LTBlocks.B_FLUX_ORDERED);
		pvd.stonecutting(DataIngredient.items(Items.PAPER), LTItems.FILLER);

		smithing(pvd, LTBlocks.B_EXTENDED.get().asItem(), Items.ENDER_EYE, LTBlocks.B_ENDER.get().asItem());
		smithing(pvd, LTBlocks.B_EXTENDED.get().asItem(), Items.CHEST, LTBlocks.B_ITEM_HOLDER.get().asItem());
		smithing(pvd, LTBlocks.B_EXTENDED.get().asItem(), Items.CRAFTING_TABLE, LTBlocks.B_CRAFT_SIDE.get().asItem());
		pvd.stonecutting(DataIngredient.items(LTBlocks.B_CRAFT_SIDE.get()), LTBlocks.B_CRAFT_CORE);

		unlock(pvd, ShapedRecipeBuilder.shaped(LTItems.LINKER.get())::unlockedBy, Items.COPPER_INGOT)
				.pattern(" AB").pattern(" BB").pattern("B  ")
				.define('A', Items.REDSTONE)
				.define('B', Items.COPPER_INGOT)
				.save(pvd);

		pvd.stonecutting(DataIngredient.items(LTItems.LINKER.get()), LTItems.VALIDATOR);
		pvd.stonecutting(DataIngredient.items(LTItems.LINKER.get()), LTItems.CLEAR);
		pvd.stonecutting(DataIngredient.items(LTItems.LINKER.get()), LTItems.ROTATE);
		pvd.stonecutting(DataIngredient.items(LTItems.LINKER.get()), LTItems.CONFIG);
		pvd.stonecutting(DataIngredient.items(LTItems.LINKER.get()), LTItems.FLUX);

		unlock(pvd, ShapedRecipeBuilder.shaped(LTItems.VALVE_UP.get(), 4)::unlockedBy, Items.QUARTZ)
				.pattern(" A ").pattern("ABA").pattern(" A ")
				.define('A', Items.REDSTONE)
				.define('B', Items.QUARTZ)
				.save(pvd);

		unlock(pvd, ShapedRecipeBuilder.shaped(LTItems.SPEED_UP_1.get(), 4)::unlockedBy, LTItems.SPEED_UP_0.get())
				.pattern("ABA").pattern("BCB").pattern("ABA")
				.define('C', Items.IRON_INGOT)
				.define('A', Items.GLOWSTONE_DUST)
				.define('B', LTItems.SPEED_UP_0.get())
				.save(pvd);

		unlock(pvd, ShapedRecipeBuilder.shaped(LTItems.SPEED_UP_2.get(), 4)::unlockedBy, LTItems.SPEED_UP_1.get())
				.pattern("ABA").pattern("BCB").pattern("ABA")
				.define('C', Items.GOLD_INGOT)
				.define('A', Items.BLAZE_POWDER)
				.define('B', LTItems.SPEED_UP_1.get())
				.save(pvd);

		unlock(pvd, ShapedRecipeBuilder.shaped(LTItems.THR_UP_1.get(), 4)::unlockedBy, LTItems.THR_UP_0.get())
				.pattern("ABA").pattern("BCB").pattern("ABA")
				.define('C', Items.IRON_INGOT)
				.define('A', Items.GLOWSTONE_DUST)
				.define('B', LTItems.THR_UP_0.get())
				.save(pvd);

		unlock(pvd, ShapedRecipeBuilder.shaped(LTItems.THR_UP_2.get(), 4)::unlockedBy, LTItems.THR_UP_1.get())
				.pattern("ABA").pattern("BCB").pattern("ABA")
				.define('C', Items.GOLD_INGOT)
				.define('A', Items.BLAZE_POWDER)
				.define('B', LTItems.THR_UP_1.get())
				.save(pvd);

		pvd.stonecutting(DataIngredient.items(LTItems.VALVE_UP.get()), LTItems.WATCH_UP);
		pvd.stonecutting(DataIngredient.items(LTItems.VALVE_UP.get()), LTItems.DIST_UP_0);
		pvd.stonecutting(DataIngredient.items(LTItems.VALVE_UP.get()), LTItems.SPEED_UP_0);
		pvd.stonecutting(DataIngredient.items(LTItems.VALVE_UP.get()), LTItems.THR_UP_0);
		pvd.stonecutting(DataIngredient.items(LTItems.VALVE_UP.get()), LTItems.DROP_UP);
		pvd.stonecutting(DataIngredient.items(LTItems.VALVE_UP.get()), LTItems.PLACE_UP);

	}

	private static ResourceLocation getID(Item item) {
		return new ResourceLocation(LaserTransport.MODID, currentFolder + ForgeRegistries.ITEMS.getKey(item).getPath());
	}

	private static ResourceLocation getID(Item item, String suffix) {
		return new ResourceLocation(LaserTransport.MODID, currentFolder + ForgeRegistries.ITEMS.getKey(item).getPath() + suffix);
	}

	private static ResourceLocation getID(String suffix) {
		return new ResourceLocation(LaserTransport.MODID, currentFolder + suffix);
	}

	private static void cross(RegistrateRecipeProvider pvd, Item core, Item side, Item out, int count) {
		unlock(pvd, new ShapedRecipeBuilder(out, count)::unlockedBy, core)
				.pattern(" S ").pattern("SCS").pattern(" S ")
				.define('S', side).define('C', core)
				.save(pvd, getID(out));
	}

	private static void full(RegistrateRecipeProvider pvd, Item core, Item side, Item corner, Item out, int count) {
		unlock(pvd, new ShapedRecipeBuilder(out, count)::unlockedBy, core)
				.pattern("CSC").pattern("SAS").pattern("CSC")
				.define('A', core).define('S', side).define('C', corner)
				.save(pvd, getID(out));
	}

	private static void circle(RegistrateRecipeProvider pvd, Item core, Item side, Item out, int count) {
		unlock(pvd, new ShapedRecipeBuilder(out, count)::unlockedBy, core)
				.pattern("SSS").pattern("SAS").pattern("SSS")
				.define('A', core).define('S', side)
				.save(pvd, getID(out));
	}

	private static void storage(RegistrateRecipeProvider pvd, ItemEntry<?> nugget, ItemEntry<?> ingot, BlockEntry<?> block) {
		storage(pvd, nugget::get, ingot::get);
		storage(pvd, ingot::get, block::get);
	}

	public static void storage(RegistrateRecipeProvider pvd, NonNullSupplier<ItemLike> from, NonNullSupplier<ItemLike> to) {
		unlock(pvd, new ShapedRecipeBuilder(to.get(), 1)::unlockedBy, from.get().asItem())
				.pattern("XXX").pattern("XXX").pattern("XXX").define('X', from.get())
				.save(pvd, getID(to.get().asItem()) + "_storage");
		unlock(pvd, new ShapelessRecipeBuilder(from.get(), 9)::unlockedBy, to.get().asItem())
				.requires(to.get()).save(pvd, getID(to.get().asItem()) + "_unpack");
	}

	private static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

	public static void smithing(RegistrateRecipeProvider pvd, Item in, Item mat, Item out) {
		unlock(pvd, UpgradeRecipeBuilder.smithing(Ingredient.of(in), Ingredient.of(mat), out)::unlocks, mat).save(pvd, getID(out));
	}

}
