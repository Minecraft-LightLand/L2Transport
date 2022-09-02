package dev.xkmc.l2transport.content.tile.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;
import dev.xkmc.l2transport.content.tile.base.AbstractNodeBlockEntity;
import dev.xkmc.l2transport.content.tile.base.IRenderableNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.List;

public class OverlayRenderer extends GuiComponent implements IGuiOverlay {

	private static void renderTooltipInternal(ForgeGui gui, PoseStack stack, List<ClientTooltipComponent> list, int x) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		Font font = Minecraft.getInstance().font;
		if (list.isEmpty()) return;
		int maxWidth = 0;
		int height = list.size() == 1 ? -2 : 0;

		for (ClientTooltipComponent clienttooltipcomponent : list) {
			int w = clienttooltipcomponent.getWidth(font);
			if (w > maxWidth) {
				maxWidth = w;
			}

			height += clienttooltipcomponent.getHeight();
		}

		int tx = x + 12;
		int ty = (gui.screenHeight - height) / 2;
		if (tx + maxWidth > gui.screenWidth) {
			tx -= 28 + maxWidth;
		}

		if (ty + height + 6 > gui.screenHeight) {
			ty = gui.screenHeight - height - 6;
		}

		stack.pushPose();
		float f = itemRenderer.blitOffset;
		itemRenderer.blitOffset = 400.0F;
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tesselator.getBuilder();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		Matrix4f matrix4f = stack.last().pose();
		int b = 0xf0100010, s = 0x505000FF, e = 0x5028007f;
		fillGradient(matrix4f, bufferbuilder, tx - 3, ty - 4, tx + maxWidth + 3, ty - 3, 400, s, s);
		fillGradient(matrix4f, bufferbuilder, tx - 3, ty + height + 3, tx + maxWidth + 3, ty + height + 4, 400, e, e);
		fillGradient(matrix4f, bufferbuilder, tx - 3, ty - 3, tx + maxWidth + 3, ty + height + 3, 400, s, e);
		fillGradient(matrix4f, bufferbuilder, tx - 4, ty - 3, tx - 3, ty + height + 3, 400, s, e);
		fillGradient(matrix4f, bufferbuilder, tx + maxWidth + 3, ty - 3, tx + maxWidth + 4, ty + height + 3, 400, s, e);
		fillGradient(matrix4f, bufferbuilder, tx - 3, ty - 3 + 1, tx - 3 + 1, ty + height + 3 - 1, 400, s, e);
		fillGradient(matrix4f, bufferbuilder, tx + maxWidth + 2, ty - 3 + 1, tx + maxWidth + 3, ty + height + 3 - 1, 400, s, e);
		fillGradient(matrix4f, bufferbuilder, tx - 3, ty - 3, tx + maxWidth + 3, ty - 3 + 1, 400, s, s);
		fillGradient(matrix4f, bufferbuilder, tx - 3, ty + height + 2, tx + maxWidth + 3, ty + height + 3, 400, e, e);
		RenderSystem.enableDepthTest();
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		BufferUploader.drawWithShader(bufferbuilder.end());
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
		MultiBufferSource.BufferSource source = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		stack.translate(0.0D, 0.0D, 400.0D);
		int iy = ty;

		for (int i2 = 0; i2 < list.size(); ++i2) {
			ClientTooltipComponent comp = list.get(i2);
			comp.renderText(font, tx, iy, matrix4f, source);
			iy += comp.getHeight() + (i2 == 0 ? 2 : 0);
		}

		source.endBatch();
		stack.popPose();
		iy = ty;

		for (int l2 = 0; l2 < list.size(); ++l2) {
			ClientTooltipComponent comp = list.get(l2);
			comp.renderImage(font, tx, iy, stack, itemRenderer, 400);
			iy += comp.getHeight() + (l2 == 0 ? 2 : 0);
		}

		itemRenderer.blitOffset = f;
	}

	private static List<ClientTooltipComponent> gatherTooltipComponents(List<MutableComponent> textElements, int mouseX, int screenWidth) {
		Font font = Minecraft.getInstance().font;

		// text wrapping
		int tooltipTextWidth = textElements.stream().mapToInt(font::width).max().orElse(0);
		boolean needsWrap = false;
		int tooltipX = mouseX + 12;
		if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
			tooltipTextWidth = screenWidth - 16 - mouseX;
			needsWrap = true;
		}
		if (screenWidth > 0 && tooltipTextWidth > screenWidth) {
			tooltipTextWidth = screenWidth;
			needsWrap = true;
		}
		int tooltipTextWidthF = tooltipTextWidth;
		if (needsWrap) {
			return textElements.stream().flatMap(text ->
					font.split(text, tooltipTextWidthF).stream().map(ClientTooltipComponent::create)).toList();
		}
		return textElements.stream().map(text -> ClientTooltipComponent.create(text.getVisualOrderText())).toList();
	}

	private static void renderText(ForgeGui gui, PoseStack stack, List<MutableComponent> text, int x) {
		renderTooltipInternal(gui, stack, gatherTooltipComponents(text, x, gui.screenWidth), x);
	}

	@Override
	public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
		if (Minecraft.getInstance().screen != null) return;
		LocalPlayer player = Proxy.getClientPlayer();
		if (player == null) return;
		var ray = RayTraceUtil.rayTraceBlock(player.level, player, player.getReachDistance());
		if (ray.getType() != HitResult.Type.BLOCK) return;
		BlockPos pos = ray.getBlockPos();
		BlockEntity entity = player.level.getBlockEntity(pos);
		if (entity instanceof IRenderableNode be) {
			renderText(gui, poseStack, be.getTooltips(), screenWidth / 2 + 16);
		}
	}

}
