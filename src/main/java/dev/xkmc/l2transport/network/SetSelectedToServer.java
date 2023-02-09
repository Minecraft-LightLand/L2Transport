package dev.xkmc.l2transport.network;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.SerialClass.SerialField;
import dev.xkmc.l2library.serial.network.SerialPacketBase;
import dev.xkmc.l2transport.content.items.select.ItemSelector;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent.Context;

@SerialClass
public class SetSelectedToServer extends SerialPacketBase {

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
            ItemSelector sel = ItemSelector.getSelection(sender);
            if (sel != null)
                sel.swap(sender, slot);
        }
    }

}
