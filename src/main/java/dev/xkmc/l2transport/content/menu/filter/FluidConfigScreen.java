package dev.xkmc.l2transport.content.menu.filter;

import dev.xkmc.l2transport.content.menu.ghost.FluidTarget;
import dev.xkmc.l2transport.init.L2Transport;
import dev.xkmc.l2transport.network.SetFluidFilterToServer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class FluidConfigScreen extends BaseConfigScreen<FluidConfigMenu> {

	public FluidConfigScreen(FluidConfigMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

	public List<FluidTarget> getFluidTargets() {
		List<FluidTarget> ans = new ArrayList<>();
		var rect = menu.sprite.getComp("grid");
		for (int y = 0; y < rect.ry; y++) {
			for (int x = 0; x < rect.rx; x++) {
				int id = y * rect.rx + x;
				ans.add(new FluidTarget(rect.x + x * rect.w + leftPos, rect.y + y * rect.h + topPos, 16, 16, stack -> addGhost(id, stack)));
			}
		}
		return ans;
	}

	public void addGhost(int ind, FluidStack stack) {
		menu.setSlotContent(ind, stack);
		L2Transport.HANDLER.toServer(new SetFluidFilterToServer(ind, stack));
	}

}
