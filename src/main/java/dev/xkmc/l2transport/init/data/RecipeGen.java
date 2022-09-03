package dev.xkmc.l2transport.init.data;

import dev.xkmc.l2library.repack.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.repack.registrate.util.DataIngredient;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2transport.init.L2Transport;
import dev.xkmc.l2transport.init.registrate.LTBlocks;
import dev.xkmc.l2transport.init.registrate.LTItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiFunction;

public class RecipeGen {

	private static final String currentFolder = "";

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		full(pvd, Items.REDSTONE_BLOCK, Items.IRON_INGOT, Items.COPPER_INGOT, LTBlocks.B_SIDED.get().asItem(), 16);
		pvd.stonecutting(DataIngredient.items(LTBlocks.B_SIDED.get()), LTBlocks.B_ITEM_SIMPLE);
		pvd.stonecutting(DataIngredient.items(LTBlocks.B_SIDED.get()), LTBlocks.B_FLUID_SIMPLE);
		pvd.stonecutting(DataIngredient.items(LTBlocks.B_SIDED.get()), LTBlocks.B_FLUX_SIMPLE);
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

		unlock(pvd, ShapedRecipeBuilder.shaped(LTItems.LINKER.get())::unlockedBy, Items.COPPER_INGOT)
				.pattern(" AB").pattern(" BB").pattern("B  ")
				.define('A', Items.REDSTONE)
				.define('B', Items.COPPER_INGOT)
				.save(pvd);

		pvd.stonecutting(DataIngredient.items(LTItems.LINKER.get()), LTItems.ROTATE);
		pvd.stonecutting(DataIngredient.items(LTItems.LINKER.get()), LTItems.VALIDATOR);
		pvd.stonecutting(DataIngredient.items(LTItems.LINKER.get()), LTItems.CLEAR);

	}

	private static ResourceLocation getID(Item item) {
		return new ResourceLocation(L2Transport.MODID, currentFolder + ForgeRegistries.ITEMS.getKey(item).getPath());
	}

	private static ResourceLocation getID(Item item, String suffix) {
		return new ResourceLocation(L2Transport.MODID, currentFolder + ForgeRegistries.ITEMS.getKey(item).getPath() + suffix);
	}

	private static ResourceLocation getID(String suffix) {
		return new ResourceLocation(L2Transport.MODID, currentFolder + suffix);
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

}
