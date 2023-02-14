package dev.xkmc.lasertransport.content.tile.base;

import dev.xkmc.l2library.util.annotation.ServerOnly;
import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.lasertransport.content.items.upgrades.UpgradeItem;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface IUpgradableBlock {

	boolean acceptUpgrade(GenericItemStack<UpgradeItem> item);

	@ServerOnly
	Optional<ItemStack> addUpgrade(GenericItemStack<UpgradeItem> item);

}
