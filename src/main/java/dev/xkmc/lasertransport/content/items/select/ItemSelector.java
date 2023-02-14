package dev.xkmc.lasertransport.content.items.select;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemSelector extends IItemSelector {

	private final List<ItemStack> list;
	private final Set<Item> set = new HashSet<>();

	public ItemSelector(ItemStack... stacks) {
		list = List.of(stacks);
		for (ItemStack stack : stacks) {
			set.add(stack.getItem());
		}
	}

	public boolean test(ItemStack stack) {
		return set.contains(stack.getItem());
	}

	public int getIndex(Player player) {
		for (int i = 0; i < list.size(); i++) {
			if (test(player.getMainHandItem())) {
				if (list.get(i).getItem() == player.getMainHandItem().getItem()) return i;
			} else if (test(player.getOffhandItem())) {
				if (list.get(i).getItem() == player.getMainHandItem().getItem()) return i;
			}
		}
		return 0;
	}

	public List<ItemStack> getList() {
		return list;
	}

}
