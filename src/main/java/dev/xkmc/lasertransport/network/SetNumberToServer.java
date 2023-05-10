package dev.xkmc.lasertransport.network;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.lasertransport.content.capability.base.INodeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;

@SerialClass
public class SetNumberToServer extends SerialPacketBase {

	@SerialClass.SerialField
	private long filter;
	@SerialClass.SerialField
	private BlockPos pos;

	/**
	 * @deprecated
	 */
	@Deprecated
	public SetNumberToServer() {
	}

	public SetNumberToServer(BlockPos pos, long filter) {
		this.pos = pos;
		this.filter = filter;
	}

	public void handle(Context ctx) {
		Player sender = ctx.getSender();
		if (sender == null) return;
		if (!sender.getLevel().isLoaded(pos)) return;
		if (!(sender.getLevel().getBlockEntity(pos) instanceof INodeBlockEntity be)) return;
		be.getConfig().setTransferLimit(filter);
		be.markDirty();
	}

}
