package dev.xkmc.l2transport.content.items.select;

import dev.xkmc.l2transport.content.capability.generic.GenericCapabilityRegistry;
import dev.xkmc.l2transport.content.items.tools.FluxFilter;
import dev.xkmc.l2transport.init.registrate.LTItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FluxSelector extends IItemSelector {

	public FluxSelector() {

	}

	@Override
	public boolean test(ItemStack stack) {
		return stack.getItem() == LTItems.FLUX.get();
	}

	@Override
	public int getIndex(Player player) {
		if (test(player.getMainHandItem())) {
			return GenericCapabilityRegistry.indexOf(FluxFilter.get(player.getMainHandItem()));
		}
		if (test(player.getOffhandItem())) {
			return GenericCapabilityRegistry.indexOf(FluxFilter.get(player.getOffhandItem()));
		}
		return 0;
	}

	@Override
	public List<ItemStack> getList() {
		return GenericCapabilityRegistry.values().stream().map(FluxFilter::of).toList();
	}
}
