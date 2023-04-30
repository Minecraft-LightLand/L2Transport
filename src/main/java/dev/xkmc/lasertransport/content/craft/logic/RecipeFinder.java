package dev.xkmc.lasertransport.content.craft.logic;

import dev.xkmc.lasertransport.compat.create.FakeMechanicalCraftContainer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;

import java.util.Optional;

public class RecipeFinder {

	public static Optional<CraftingRecipe> getRecipes(Level level, DelegatedCraftContainer cont) {
		var opt = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, cont, level);
		if (opt.isEmpty()) {
			if (ModList.get().isLoaded("create")) {
				opt = FakeMechanicalCraftContainer.getRecipes(level, cont);
			}
		}
		return opt;
	}

}
