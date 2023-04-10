package dev.xkmc.lasertransport.content.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import dev.xkmc.l2library.base.overlay.OverlayUtils;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.lasertransport.content.craft.tile.CraftCoreBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
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
		var ray = RayTraceUtil.rayTraceBlock(player.level, player, player.getReachDistance());
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
			renderTooltipInternal(util, poseStack, screenWidth, screenHeight, list);
		}
	}

	private static void renderTooltipInternal(OverlayUtils utils, PoseStack poseStack, int sw, int sh, List<ClientTooltipComponent> list) {
		//TODO move to library
		if (list.isEmpty()) return;
		Font font = Minecraft.getInstance().font;
		ItemRenderer ir = Minecraft.getInstance().getItemRenderer();
		int w = 0;
		int h = list.size() == 1 ? -2 : 0;
		for (ClientTooltipComponent ctc : list) {
			int k = ctc.getWidth(font);
			if (k > w) {
				w = k;
			}
			h += ctc.getHeight();
		}

		int x = sw / 2 + MARGIN;
		int y = Math.round((float) (sh - h) / 2.0F);
		if (x + w > sw) {
			x -= 28 + w;
		}

		if (y + h + 6 > sh) {
			y = sh - h - 6;
		}

		poseStack.pushPose();
		float f = ir.blitOffset;
		int z = 400;
		ir.blitOffset = z;
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tesselator.getBuilder();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		Matrix4f matrix4f = poseStack.last().pose();
		fillGradient(matrix4f, bufferbuilder, x - 3, y - 4, x + w + 3, y - 3, z, utils.bg, utils.bg);
		fillGradient(matrix4f, bufferbuilder, x - 3, y + h + 3, x + w + 3, y + h + 4, z, utils.bg, utils.bg);
		fillGradient(matrix4f, bufferbuilder, x - 3, y - 3, x + w + 3, y + h + 3, z, utils.bg, utils.bg);
		fillGradient(matrix4f, bufferbuilder, x - 4, y - 3, x - 3, y + h + 3, z, utils.bg, utils.bg);
		fillGradient(matrix4f, bufferbuilder, x + w + 3, y - 3, x + w + 4, y + h + 3, z, utils.bg, utils.bg);
		fillGradient(matrix4f, bufferbuilder, x - 3, y - 3 + 1, x - 3 + 1, y + h + 3 - 1, z, utils.bs, utils.be);
		fillGradient(matrix4f, bufferbuilder, x + w + 2, y - 3 + 1, x + w + 3, y + h + 3 - 1, z, utils.bs, utils.be);
		fillGradient(matrix4f, bufferbuilder, x - 3, y - 3, x + w + 3, y - 3 + 1, z, utils.bs, utils.bs);
		fillGradient(matrix4f, bufferbuilder, x - 3, y + h + 2, x + w + 3, y + h + 3, z, utils.be, utils.be);
		RenderSystem.enableDepthTest();
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		BufferUploader.drawWithShader(bufferbuilder.end());
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
		MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		poseStack.translate(0.0D, 0.0D, z);
		int iy = y;
		for (int i = 0; i < list.size(); ++i) {
			ClientTooltipComponent ctc = list.get(i);
			ctc.renderText(font, x, iy, matrix4f, buffer);
			iy += ctc.getHeight() + (i == 0 ? 2 : 0);
		}
		buffer.endBatch();
		poseStack.popPose();
		iy = y;
		for (int i = 0; i < list.size(); ++i) {
			ClientTooltipComponent ctc = list.get(i);
			ctc.renderImage(font, x, iy, poseStack, ir, 400);
			iy += ctc.getHeight() + (i == 0 ? 2 : 0);
		}
		ir.blitOffset = f;
	}

}
