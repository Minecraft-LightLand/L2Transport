package dev.xkmc.lasertransport.content.client.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.base.overlay.OverlayUtils;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.lasertransport.content.tile.base.ITooltipNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import javax.annotation.Nullable;

public class NodeInfoOverlay extends GuiComponent implements IGuiOverlay {

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
		if (NumberSetOverlay.isScreenOn()) return;
		if (getTarget() instanceof ITooltipNode be) {
			OverlayUtils util = new OverlayUtils(screenWidth, screenHeight);
			util.renderLongText(gui, poseStack,
					screenWidth / 2 + MARGIN, -1, (screenWidth) / 2 - MARGIN * 2,
					be.getTooltips().build());
		}
	}

}
