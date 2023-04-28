package dev.xkmc.lasertransport.events;

import dev.xkmc.lasertransport.content.items.select.ItemSelector;
import dev.xkmc.lasertransport.init.data.TagGen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemConvertEvents {

	public static ItemStack convert(ItemStack stack, Player player) {
		if (!stack.is(TagGen.SELECTABLE)) {
			return stack;
		}
		ItemSelector ans = null;
		for (ItemSelector selector : ItemSelector.SELECTORS) {
			if (selector.test(stack)) {
				ans = selector;
				break;
			}
		}
		if (ans == null) {
			return stack;
		}
		Inventory inv = player.getInventory();
		for (ItemStack choice : ans.getList()) {
			if (inv.hasRemainingSpaceForItem(inv.getItem(inv.selected), choice)) {
				return choice;
			}
		}
		for (ItemStack choice : ans.getList()) {
			if (inv.hasRemainingSpaceForItem(inv.getItem(40), choice)) {
				return choice;
			}
		}
		for (ItemStack choice : ans.getList()) {
			for (int i = 0; i < inv.items.size(); ++i) {
				if (inv.hasRemainingSpaceForItem(inv.items.get(i), choice)) {
					return choice;
				}
			}
		}
		return stack;
	}

	@SubscribeEvent
	public static void addItemToInventory(PlayerEvent.ItemPickupEvent event) {
		ItemStack prev = event.getStack();
		ItemStack next = convert(prev, event.getEntity());
		if (next != prev) {
			event.getOriginalEntity().setItem(next);
		}
	}

}
