package dev.xkmc.lasertransport.events;

import dev.xkmc.l2library.init.data.L2Keys;
import dev.xkmc.l2library.init.events.select.ISelectionListener;
import dev.xkmc.l2library.init.events.select.SetSelectedToServer;
import dev.xkmc.lasertransport.content.capability.base.INodeBlockEntity;
import dev.xkmc.lasertransport.content.client.overlay.NodeInfoOverlay;
import dev.xkmc.lasertransport.content.client.overlay.NumberSetOverlay;
import dev.xkmc.lasertransport.init.LaserTransport;
import dev.xkmc.lasertransport.init.registrate.LTItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.BooleanSupplier;

public class NumericSel implements ISelectionListener {

	public static final NumericSel INSTANCE = new NumericSel();

	private static final ResourceLocation ID = new ResourceLocation(LaserTransport.MODID, "numeric");

	@Override
	public ResourceLocation getID() {
		return ID;
	}

	@Override
	public boolean isClientActive(Player player) {
		if (Minecraft.getInstance().screen != null) return false;
		if (Screen.hasShiftDown() || Screen.hasAltDown()) return false;
		return NodeInfoOverlay.getTarget() instanceof INodeBlockEntity &&
				player.getMainHandItem().is(LTItems.CONFIG.get());
	}

	@Override
	public void handleServerSetSelection(SetSelectedToServer packet, Player player) {

	}

	@Override
	public boolean handleClientScroll(int diff, Player player) {
		while (diff > 0) {
			NumberSetOverlay.up();
			diff--;
		}
		while (diff < 0) {
			NumberSetOverlay.down();
			diff++;
		}
		return true;
	}

	@Override
	public void handleClientKey(L2Keys key, Player player) {
		if (key == L2Keys.UP) {
			NumberSetOverlay.up();
		} else if (key == L2Keys.DOWN) {
			NumberSetOverlay.down();
		} else if (key == L2Keys.LEFT) {
			NumberSetOverlay.left();
		} else if (key == L2Keys.RIGHT) {
			NumberSetOverlay.right();
		}
	}

	@Override
	public boolean handleClientNumericKey(int i, BooleanSupplier click) {
		return false;
	}

	@Override
	public boolean scrollBypassShift() {
		return true;
	}
}
