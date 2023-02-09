package dev.xkmc.l2transport.content.items.select;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.annotation.ServerOnly;
import dev.xkmc.l2transport.init.L2Transport;
import dev.xkmc.l2transport.network.SetSelectedToServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ItemSelector {

	private static final List<ItemSelector> LIST = new ArrayList<>();

	@Nullable
	public static ItemSelector getSelection(Player player) {
		for (var sel : LIST) {
			if (sel.predicate.test(player.getMainHandItem().getItem()))
				return sel;
			if (sel.predicate.test(player.getOffhandItem().getItem()))
				return sel;
		}
		return null;
	}

	public final int index;
	private final List<ItemStack> list;
	private final Predicate<Item> predicate;

	public ItemSelector(Predicate<Item> predicate, ItemStack... stacks) {
		this.predicate = predicate;
		list = List.of(stacks);
		index = LIST.size();
		LIST.add(this);
	}

	public ItemSelector(ItemStack... stacks) {
		list = List.of(stacks);
		index = LIST.size();
		LIST.add(this);
		this.predicate = e -> getList().stream().anyMatch(x -> e == x.getItem());
	}

	@ServerOnly
	public void swap(Player sender, int index) {
		index = (index + list.size()) % list.size();
		if (index < 0) return;
		if (predicate.test(sender.getMainHandItem().getItem())) {
			ItemStack stack = list.get(index).copy();
			stack.setCount(sender.getMainHandItem().getCount());
			sender.setItemInHand(InteractionHand.MAIN_HAND, stack);
		} else if (predicate.test(sender.getOffhandItem().getItem())) {
			ItemStack stack = list.get(index).copy();
			stack.setCount(sender.getOffhandItem().getCount());
			sender.setItemInHand(InteractionHand.OFF_HAND, stack);
		}
	}

	public int getIndex(Player player) {
		for (int i = 0; i < list.size(); i++) {
			if (predicate.test(player.getMainHandItem().getItem())) {
				if (list.get(i).getItem() == player.getMainHandItem().getItem()) return i;
			} else if (predicate.test(player.getOffhandItem().getItem())) {
				if (list.get(i).getItem() == player.getMainHandItem().getItem()) return i;
			}
		}
		return 0;
	}

	@OnlyIn(Dist.CLIENT)
	public void move(int i) {
		int index = getIndex(Proxy.getClientPlayer());
		index = (index + list.size() + i) % list.size();
		L2Transport.HANDLER.toServer(new SetSelectedToServer(index));
	}

	public List<ItemStack> getList() {
		return list;
	}

}
