package dev.xkmc.lasertransport.content.client.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.base.overlay.OverlayUtils;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.lasertransport.content.craft.tile.CraftCoreBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import javax.annotation.Nullable;
import java.util.List;

public class CraftTooltipOverlay extends GuiComponent implements IGuiOverlay {

	public static final int MARGIN = 24;

	@Nullable
	public static BlockEntity getTarget() {
		if (Minecraft.getInstance().screen != null) return null;
		LocalPlayer player = Proxy.getClientPlayer();
		if (player == null) return null;
		var ray = RayTraceUtil.rayTraceBlock(player.level, player, player.getBlockReach());
		if (ray.getType() != HitResult.Type.BLOCK) return null;
		BlockPos pos = ray.getBlockPos();
		return player.level.getBlockEntity(pos);
	}

	@Override
	public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
		if (getTarget() instanceof CraftCoreBlockEntity be) {
			OverlayUtils util = new OverlayUtils(screenWidth, screenHeight);
			util.bg = 0xffc6c6c6;
			List<ClientTooltipComponent> list = be.getTooltip().stream()
					.map(e -> e.map(t -> ClientTooltipComponent.create(t.getVisualOrderText()),
							ClientTooltipComponent::create)).toList();
			util.renderTooltipInternal(poseStack, list, -1, -1);
		}
	}

}
