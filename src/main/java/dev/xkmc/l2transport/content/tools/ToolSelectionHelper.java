package dev.xkmc.l2transport.content.tools;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.annotation.ServerOnly;
import dev.xkmc.l2transport.init.L2Transport;
import dev.xkmc.l2transport.init.registrate.LTItems;
import dev.xkmc.l2transport.network.SetSelectedToServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class ToolSelectionHelper {

	public static final List<ItemStack> LIST = List.of(
			LTItems.LINKER.asStack(),
			LTItems.VALIDATOR.asStack(),
			LTItems.CLEAR.asStack(),
			LTItems.ROTATE.asStack(),
			LTItems.CONFIG.asStack());

	@ServerOnly
	public static void swap(Player sender, int index) {
		index = (index + LIST.size()) % LIST.size();
		if (index < 0) return;
		if (sender.getMainHandItem().getItem() instanceof ILinker) {
			sender.setItemInHand(InteractionHand.MAIN_HAND, LIST.get(index).copy());
		} else if (sender.getOffhandItem().getItem() instanceof ILinker) {
			sender.setItemInHand(InteractionHand.OFF_HAND, LIST.get(index).copy());
		}
	}

	public static int getIndex(Player player) {
		for (int i = 0; i < LIST.size(); i++) {
			if (player.getMainHandItem().getItem() instanceof ILinker linker) {
				if (LIST.get(i).getItem() == linker) return i;
			} else if (player.getOffhandItem().getItem() instanceof ILinker linker) {
				if (LIST.get(i).getItem() == linker) return i;
			}
		}
		return 0;
	}

	@OnlyIn(Dist.CLIENT)
	public static void move(int i) {
		int index = getIndex(Proxy.getClientPlayer());
		index = (index + LIST.size() + i) % LIST.size();
		L2Transport.HANDLER.toServer(new SetSelectedToServer(index));
	}
}
