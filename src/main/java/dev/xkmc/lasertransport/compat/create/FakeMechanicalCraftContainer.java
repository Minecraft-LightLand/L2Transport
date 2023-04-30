package dev.xkmc.lasertransport.compat.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.components.crafter.MechanicalCraftingInventory;
import com.simibubi.create.content.contraptions.components.crafter.RecipeGridHandler;
import dev.xkmc.lasertransport.content.craft.logic.CraftGrid;
import dev.xkmc.lasertransport.content.craft.logic.DelegatedCraftContainer;
import dev.xkmc.lasertransport.init.registrate.LTItems;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FakeMechanicalCraftContainer extends MechanicalCraftingInventory {

	public static Optional<CraftingRecipe> getRecipes(Level level, DelegatedCraftContainer cont) {
		FakeMechanicalCraftContainer fake = new FakeMechanicalCraftContainer(cont.grid);
		return level.getRecipeManager().getRecipeFor(AllRecipeTypes.MECHANICAL_CRAFTING.getType(), fake, level);
	}

	private final CraftGrid grid;
	private final boolean isEmpty;

	public final int min;

	@SuppressWarnings("ConstantConditions")
	public FakeMechanicalCraftContainer(CraftGrid grid) {
		super(new RecipeGridHandler.GroupedItems());
		this.grid = grid;
		boolean empty = true;
		boolean has_ingredient = false;
		int min = -1;
		for (var arr : grid.list()) {
			for (var ar : arr) {
				empty &= ar.stack().isEmpty();
				has_ingredient |= !ar.stack().isEmpty() && !ar.stack().is(LTItems.FILLER.get());
				int count = ar.stack().getCount();
				min = count == 0 ? min : min < 0 ? count : Math.min(count, min);
			}
		}
		this.isEmpty = empty || !has_ingredient;
		this.min = min;
	}

	@Override
	public int getWidth() {
		return grid.width();
	}

	@Override
	public int getHeight() {
		return grid.height();
	}

	public int getContainerSize() {
		return getWidth() * grid.height();
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	@NotNull
	public ItemStack getItem(int slot) {
		return grid.list()[slot / getWidth()][slot % getWidth()].stack();
	}

	@NotNull
	public ItemStack removeItemNoUpdate(int slot) {
		return ItemStack.EMPTY;
	}

	@NotNull
	public ItemStack removeItem(int slot, int count) {
		return ItemStack.EMPTY;
	}

	public void setItem(int slot, @NotNull ItemStack stack) {
	}

	public void clearContent() {
	}

	public void fillStackedContents(@NotNull StackedContents helper) {
	}

}
