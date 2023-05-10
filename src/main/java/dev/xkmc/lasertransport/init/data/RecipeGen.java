package dev.xkmc.lasertransport.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2library.serial.recipe.AbstractSmithingRecipe;
import dev.xkmc.lasertransport.init.LaserTransport;
import dev.xkmc.lasertransport.init.registrate.LTBlocks;
import dev.xkmc.lasertransport.init.registrate.LTItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@SuppressWarnings({"unused", "ConstantConditions"})
public class RecipeGen {

	private static final String currentFolder = "";

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		full(pvd, Items.REDSTONE_BLOCK, Items.IRON_INGOT, Items.COPPER_INGOT, LTBlocks.B_EXTENDED.get().asItem(), 16);
		convert(pvd, LTBlocks.B_EXTENDED,
				LTBlocks.B_ITEM_SIMPLE,
				LTBlocks.B_FLUID_SIMPLE,
				LTBlocks.B_FLUX_SIMPLE);
		convert(pvd, LTBlocks.B_ITEM_SIMPLE,
				LTBlocks.B_ITEM_RETRIEVE,
				LTBlocks.B_ITEM_ORDERED,
				LTBlocks.B_ITEM_DISTRIBUTE,
				LTBlocks.B_ITEM_SYNCED);
		convert(pvd, LTBlocks.B_FLUID_SIMPLE,
				LTBlocks.B_FLUID_RETRIEVE,
				LTBlocks.B_FLUID_ORDERED,
				LTBlocks.B_FLUID_DISTRIBUTE,
				LTBlocks.B_FLUID_SYNCED);
		convert(pvd, LTBlocks.B_FLUX_SIMPLE,
				LTBlocks.B_FLUX_RETRIEVE,
				LTBlocks.B_FLUX_SYNCED,
				LTBlocks.B_FLUX_DISTRIBUTE,
				LTBlocks.B_FLUX_ORDERED);
		convert(pvd, LTItems.LINKER,
				LTItems.VALIDATOR,
				LTItems.CLEAR,
				LTItems.ROTATE,
				LTItems.CONFIG,
				LTItems.FLUX);
		convert(pvd, LTItems.VALVE_UP,
				LTItems.WATCH_UP,
				LTItems.DIST_UP_0,
				LTItems.SPEED_UP_0,
				LTItems.THR_UP_0,
				LTItems.DROP_UP,
				LTItems.PLACE_UP);
		convert(pvd, () -> Items.PAPER, LTItems.FILLER);
		convert(pvd, LTBlocks.B_CRAFT_SIDE, LTBlocks.B_CRAFT_CORE);

		smithing(pvd, LTBlocks.B_EXTENDED.get().asItem(), Items.ENDER_EYE, LTBlocks.B_ENDER.get().asItem());
		smithing(pvd, LTBlocks.B_EXTENDED.get().asItem(), Items.CHEST, LTBlocks.B_ITEM_HOLDER.get().asItem());
		smithing(pvd, LTBlocks.B_EXTENDED.get().asItem(), Items.CRAFTING_TABLE, LTBlocks.B_CRAFT_SIDE.get().asItem());

		unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, LTItems.LINKER.get())::unlockedBy, Items.COPPER_INGOT)
				.pattern(" AB").pattern(" BB").pattern("B  ")
				.define('A', Items.REDSTONE)
				.define('B', Items.COPPER_INGOT)
				.save(pvd);

		unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, LTItems.VALVE_UP.get(), 4)::unlockedBy, Items.QUARTZ)
				.pattern(" A ").pattern("ABA").pattern(" A ")
				.define('A', Items.REDSTONE)
				.define('B', Items.QUARTZ)
				.save(pvd);

		unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, LTItems.SPEED_UP_1.get(), 4)::unlockedBy, LTItems.SPEED_UP_0.get())
				.pattern("ABA").pattern("BCB").pattern("ABA")
				.define('C', Items.IRON_INGOT)
				.define('A', Items.GLOWSTONE_DUST)
				.define('B', LTItems.SPEED_UP_0.get())
				.save(pvd);

		unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, LTItems.SPEED_UP_2.get(), 4)::unlockedBy, LTItems.SPEED_UP_1.get())
				.pattern("ABA").pattern("BCB").pattern("ABA")
				.define('C', Items.GOLD_INGOT)
				.define('A', Items.BLAZE_POWDER)
				.define('B', LTItems.SPEED_UP_1.get())
				.save(pvd);

		unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, LTItems.THR_UP_1.get(), 4)::unlockedBy, LTItems.THR_UP_0.get())
				.pattern("ABA").pattern("BCB").pattern("ABA")
				.define('C', Items.IRON_INGOT)
				.define('A', Items.GLOWSTONE_DUST)
				.define('B', LTItems.THR_UP_0.get())
				.save(pvd);

		unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, LTItems.THR_UP_2.get(), 4)::unlockedBy, LTItems.THR_UP_1.get())
				.pattern("ABA").pattern("BCB").pattern("ABA")
				.define('C', Items.GOLD_INGOT)
				.define('A', Items.BLAZE_POWDER)
				.define('B', LTItems.THR_UP_1.get())
				.save(pvd);

	}

	@SafeVarargs
	private static void convert(RegistrateRecipeProvider pvd, Supplier<? extends ItemLike> a, Supplier<? extends ItemLike>... bs) {
		for (var b : bs) {
			pvd.stonecutting(DataIngredient.items(a.get()), RecipeCategory.MISC, b);
			ResourceLocation id = getID(a.get().asItem(), "_from_" + ForgeRegistries.ITEMS.getKey(b.get().asItem()).getPath());
			pvd.singleItemUnfinished(DataIngredient.items(b.get()), RecipeCategory.MISC, a, 1, 1).save(pvd, id);
		}
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
		unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, out, count)::unlockedBy, core)
				.pattern(" S ").pattern("SCS").pattern(" S ")
				.define('S', side).define('C', core)
				.save(pvd, getID(out));
	}

	private static void full(RegistrateRecipeProvider pvd, Item core, Item side, Item corner, Item out, int count) {
		unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, out, count)::unlockedBy, core)
				.pattern("CSC").pattern("SAS").pattern("CSC")
				.define('A', core).define('S', side).define('C', corner)
				.save(pvd, getID(out));
	}

	private static void circle(RegistrateRecipeProvider pvd, Item core, Item side, Item out, int count) {
		unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, out, count)::unlockedBy, core)
				.pattern("SSS").pattern("SAS").pattern("SSS")
				.define('A', core).define('S', side)
				.save(pvd, getID(out));
	}

	private static void storage(RegistrateRecipeProvider pvd, ItemEntry<?> nugget, ItemEntry<?> ingot, BlockEntry<?> block) {
		storage(pvd, nugget::get, ingot::get);
		storage(pvd, ingot::get, block::get);
	}

	public static void storage(RegistrateRecipeProvider pvd, NonNullSupplier<ItemLike> from, NonNullSupplier<ItemLike> to) {
		unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, to.get(), 1)::unlockedBy, from.get().asItem())
				.pattern("XXX").pattern("XXX").pattern("XXX").define('X', from.get())
				.save(pvd, getID(to.get().asItem()) + "_storage");
		unlock(pvd, new ShapelessRecipeBuilder(RecipeCategory.MISC, from.get(), 9)::unlockedBy, to.get().asItem())
				.requires(to.get()).save(pvd, getID(to.get().asItem()) + "_unpack");
	}

	private static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}

	@SuppressWarnings("removal")
	public static void smithing(RegistrateRecipeProvider pvd, Item in, Item mat, Item out) {
		unlock(pvd, SmithingTransformRecipeBuilder.smithing(AbstractSmithingRecipe.TEMPLATE_PLACEHOLDER,
				Ingredient.of(in), Ingredient.of(mat), RecipeCategory.MISC, out)::unlocks, mat).save(pvd, getID(out));
		unlock(pvd, LegacyUpgradeRecipeBuilder.smithing(Ingredient.of(in), Ingredient.of(mat),
				RecipeCategory.MISC, out)::unlocks, mat).save(pvd, getID(out, "_old"));
	}

}
