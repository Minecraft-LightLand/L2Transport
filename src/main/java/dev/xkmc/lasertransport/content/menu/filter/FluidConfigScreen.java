package dev.xkmc.lasertransport.content.menu.filter;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.lasertransport.content.menu.container.FluidContainer;
import dev.xkmc.lasertransport.content.menu.ghost.FluidTarget;
import dev.xkmc.lasertransport.init.LaserTransport;
import dev.xkmc.lasertransport.network.SetFluidFilterToServer;
import dev.xkmc.lasertransport.util.ItemFluidRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
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
		LaserTransport.HANDLER.toServer(new SetFluidFilterToServer(ind, stack));
	}

	@Override
	public void renderSlot(PoseStack poseStack, Slot slot) {
		if (slot.container instanceof FluidContainer) {
			poseStack.pushPose();
			poseStack.translate(slot.x, slot.y, 0);
			this.setBlitOffset(100);
			RenderSystem.enableDepthTest();
			ItemFluidRenderer.RENDERER.render(poseStack, menu.getConfig().getFluid(slot.getContainerSlot()));
			this.setBlitOffset(0);
			poseStack.popPose();
		} else {
			super.renderSlot(poseStack, slot);
		}
	}

}
