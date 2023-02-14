package dev.xkmc.lasertransport.network;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.network.SerialPacketBase;
import dev.xkmc.lasertransport.content.menu.ghost.IFluidConfigMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class SetFluidFilterToServer extends SerialPacketBase {

	@SerialClass.SerialField
	private FluidStack stack;
	@SerialClass.SerialField
	private int slot;

	@Deprecated
	public SetFluidFilterToServer() {

	}

	public SetFluidFilterToServer(int slot, FluidStack stack) {
		this.slot = slot;
		this.stack = stack;
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		ServerPlayer sender = context.getSender();
		if (sender == null) return;
		if (sender.containerMenu instanceof IFluidConfigMenu menu) {
			menu.setSlotContent(slot, stack);
		}
	}

}
