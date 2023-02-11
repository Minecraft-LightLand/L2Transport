package dev.xkmc.l2transport.network;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.network.SerialPacketBase;
import dev.xkmc.l2transport.content.menu.ItemConfigMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class SetItemFilterToServer extends SerialPacketBase {

	@SerialClass.SerialField
	private ItemStack stack;
	@SerialClass.SerialField
	private int slot;

	private SetItemFilterToServer() {

	}

	public SetItemFilterToServer(int slot, ItemStack stack) {
		this.slot = slot;
		this.stack = stack;
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		ServerPlayer sender = context.getSender();
		if (sender == null) return;
		if (sender.containerMenu instanceof ItemConfigMenu menu) {
			menu.setSlotContent(slot, stack);
		}
	}
}
