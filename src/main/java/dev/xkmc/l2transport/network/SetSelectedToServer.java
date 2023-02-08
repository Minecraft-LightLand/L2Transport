package dev.xkmc.l2transport.network;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.SerialClass.SerialField;
import dev.xkmc.l2library.serial.network.SerialPacketBase;
import dev.xkmc.l2transport.content.tools.ToolSelectionHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;

@SerialClass
public class SetSelectedToServer extends SerialPacketBase {
    public static final int UP = -1;
    public static final int DOWN = -2;
    public static final int SWAP = -3;

    @SerialField
    private int slot;

    /**
     * @deprecated
     */
    @Deprecated
    public SetSelectedToServer() {
    }

    public SetSelectedToServer(int slot) {
        this.slot = slot;
    }

    public void handle(Context ctx) {
        Player sender = ctx.getSender();
        if (sender != null) {
            ToolSelectionHelper.swap(sender, slot);
        }
    }

}
