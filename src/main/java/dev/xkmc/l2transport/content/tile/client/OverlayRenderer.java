package dev.xkmc.l2transport.content.tile.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.Either;
import com.mojang.math.Matrix4f;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.l2transport.content.tile.base.AbstractNodeBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OverlayRenderer extends GuiComponent implements IGuiOverlay {

	private static void renderTooltipInternal(ForgeGui gui, PoseStack stack, List<ClientTooltipComponent> list, int x, int y) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		Font font = Minecraft.getInstance().font;
		if (list.isEmpty()) return;
		int i = 0;
		int j = list.size() == 1 ? -2 : 0;

		for (ClientTooltipComponent clienttooltipcomponent : list) {
			int k = clienttooltipcomponent.getWidth(font);
			if (k > i) {
				i = k;
			}

			j += clienttooltipcomponent.getHeight();
		}

		int j2 = x + 12;
		int k2 = y - 12;
		if (j2 + i > gui.screenWidth) {
			j2 -= 28 + i;
		}

		if (k2 + j + 6 > gui.screenHeight) {
			k2 = gui.screenHeight - j - 6;
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
		fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 - 4, j2 + i + 3, k2 - 3, 400, s, s);
		fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 + j + 3, j2 + i + 3, k2 + j + 4, 400, e, e);
		fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 - 3, j2 + i + 3, k2 + j + 3, 400, s, e);
		fillGradient(matrix4f, bufferbuilder, j2 - 4, k2 - 3, j2 - 3, k2 + j + 3, 400, s, e);
		fillGradient(matrix4f, bufferbuilder, j2 + i + 3, k2 - 3, j2 + i + 4, k2 + j + 3, 400, s, e);
		fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + j + 3 - 1, 400, s, e);
		fillGradient(matrix4f, bufferbuilder, j2 + i + 2, k2 - 3 + 1, j2 + i + 3, k2 + j + 3 - 1, 400, s, e);
		fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 - 3, j2 + i + 3, k2 - 3 + 1, 400, s, s);
		fillGradient(matrix4f, bufferbuilder, j2 - 3, k2 + j + 2, j2 + i + 3, k2 + j + 3, 400, e, e);
		RenderSystem.enableDepthTest();
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		BufferUploader.drawWithShader(bufferbuilder.end());
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
		MultiBufferSource.BufferSource source = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		stack.translate(0.0D, 0.0D, 400.0D);
		int l1 = k2;

		for (int i2 = 0; i2 < list.size(); ++i2) {
			ClientTooltipComponent comp = list.get(i2);
			comp.renderText(font, j2, l1, matrix4f, source);
			l1 += comp.getHeight() + (i2 == 0 ? 2 : 0);
		}

		source.endBatch();
		stack.popPose();
		l1 = k2;

		for (int l2 = 0; l2 < list.size(); ++l2) {
			ClientTooltipComponent comp = list.get(l2);
			comp.renderImage(font, j2, l1, stack, itemRenderer, 400);
			l1 += comp.getHeight() + (l2 == 0 ? 2 : 0);
		}

		itemRenderer.blitOffset = f;
	}

	private static List<ClientTooltipComponent> gatherTooltipComponents(List<? extends FormattedText> textElements, int mouseX, int screenWidth) {
		Font font = Minecraft.getInstance().font;
		List<Either<FormattedText, TooltipComponent>> elements = textElements.stream()
				.map((Function<FormattedText, Either<FormattedText, TooltipComponent>>) Either::left)
				.collect(Collectors.toCollection(ArrayList::new));

		//var event = new RenderTooltipEvent.GatherComponents(stack, screenWidth, screenHeight, elements, -1);

		// text wrapping
		int tooltipTextWidth = elements.stream()
				.mapToInt(either -> either.map(font::width, component -> 0))
				.max()
				.orElse(0);

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
			return elements.stream()
					.flatMap(either -> either.map(
							text -> font.split(text, tooltipTextWidthF).stream().map(ClientTooltipComponent::create),
							component -> Stream.of(ClientTooltipComponent.create(component))
					))
					.toList();
		}
		return elements.stream()
				.map(either -> either.map(
						text -> ClientTooltipComponent.create(text instanceof Component ? ((Component) text).getVisualOrderText() : Language.getInstance().getVisualOrder(text)),
						ClientTooltipComponent::create
				))
				.toList();
	}

	private static void renderText(ForgeGui gui, PoseStack stack, List<MutableComponent> text, int x, int y) {
		renderTooltipInternal(gui, stack, gatherTooltipComponents(text, x, gui.screenWidth), x, y);
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
		if (entity instanceof AbstractNodeBlockEntity<?> be) {
			renderText(gui, poseStack, be.getTooltips(), screenWidth / 2 + 16, screenHeight / 2 + 16);
		}
	}
}
