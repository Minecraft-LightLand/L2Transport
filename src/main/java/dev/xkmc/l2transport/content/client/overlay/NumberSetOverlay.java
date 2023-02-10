package dev.xkmc.l2transport.content.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;
import dev.xkmc.l2transport.content.configurables.ConfigConnectorType;
import dev.xkmc.l2transport.content.configurables.NumericAdjustor;
import dev.xkmc.l2transport.init.registrate.LTItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class NumberSetOverlay extends GuiComponent implements IGuiOverlay {

	private static final int Y_OFFSET = 36, BG = 0x7f100010, TEXT_PADDING_X = 5, TEXT_PADDING_Y = -2, SPACING = 4, LINE = 12;

	public static boolean isScreenOn() {
		if (ShiftManager.isAlternate()) return false;
		if (Proxy.getClientPlayer().isShiftKeyDown()) return false;
		return NodeInfoOverlay.getTarget() instanceof INodeBlockEntity && Proxy.getClientPlayer().getMainHandItem().is(LTItems.CONFIG.get());
	}

	private static BlockPos cache = null;
	private static int scale = 0;

	public static void up() {
		if (!(NodeInfoOverlay.getTarget() instanceof INodeBlockEntity be)) return;
		NumericAdjustor adj = be.getConfig().getTransferConfig();
		long step = adj.scale().getStep(scale);
		long current = adj.getTransferDisplay();
		long add = Math.min(current + step, Math.max(current, adj.getMaxFilter()));
		if (add != current) adj.setVal(add);
	}

	public static void down() {
		if (!(NodeInfoOverlay.getTarget() instanceof INodeBlockEntity be)) return;
		NumericAdjustor adj = be.getConfig().getTransferConfig();
		long step = adj.scale().getStep(scale);
		long current = adj.getTransferDisplay();
		ConfigConnectorType type = adj.config().getType();
		long sub = Math.max(current - step, type.getMinFilter());
		if (sub != current) adj.setVal(sub);
	}

	public static void left() {
		if (scale > 0) scale--;
	}

	public static void right() {
		if (!(NodeInfoOverlay.getTarget() instanceof INodeBlockEntity be)) return;
		NumericAdjustor adj = be.getConfig().getTransferConfig();
		long right = adj.scale().getStep(scale + 1);
		if (right > adj.getMaxFilter()) return;
		scale++;
	}

	@Override
	public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
		if (!isScreenOn()) return;
		if (!(NodeInfoOverlay.getTarget() instanceof INodeBlockEntity be)) return;
		if (cache == null || !cache.equals(be.getThis().getBlockPos())) {
			scale = 0;
			cache = be.getThis().getBlockPos();
		}
		NumericAdjustor adj = be.getConfig().getTransferConfig();
		ConfigConnectorType type = adj.config().getType();
		long current = adj.getTransferDisplay();
		long step = adj.scale().getStep(scale);
		long add = Math.min(current + step, Math.max(current, adj.getMaxFilter()));
		long sub = Math.max(current - step, type.getMinFilter());
		long left = scale == 0 ? 0 : adj.scale().getStep(scale - 1);
		long right = adj.scale().getStep(scale + 1);
		if (right > adj.getMaxFilter()) right = 0;
		MutableComponent preC = type.getFilterDesc();
		MutableComponent addC = add == current ? null : Component.literal(adj.scale().format(add)).withStyle(ChatFormatting.DARK_AQUA);
		MutableComponent subC = sub == current ? null : Component.literal(adj.scale().format(sub)).withStyle(ChatFormatting.DARK_AQUA);
		MutableComponent curC = Component.literal(adj.scale().format(current)).withStyle(ChatFormatting.AQUA);
		MutableComponent lefC = left == 0 ? null : Component.literal(adj.scale().format(left)).withStyle(ChatFormatting.GRAY);
		MutableComponent ritC = right == 0 ? null : Component.literal(adj.scale().format(right)).withStyle(ChatFormatting.GRAY);
		MutableComponent scaC = Component.literal(adj.scale().format(step)).withStyle(ChatFormatting.WHITE);
		int addW = addC == null ? 0 : width(gui, addC);
		int subW = subC == null ? 0 : width(gui, subC);
		int curW = width(gui, curC);
		int preW = width(gui, preC);
		int lefW = lefC == null ? 0 : width(gui, lefC);
		int ritW = ritC == null ? 0 : width(gui, ritC);
		int scaW = width(gui, scaC);
		int verW = Math.max(curW, Math.max(addW, subW)) + TEXT_PADDING_X * 2;
		int horW = Math.max(scaW, Math.max(lefW, ritW)) + TEXT_PADDING_X * 2;

		int dx = screenWidth / 2;
		int dy = screenHeight / 2 + Y_OFFSET;

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tesselator.getBuilder();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		Matrix4f matrix4f = poseStack.last().pose();

		fillGradient(matrix4f, bufferbuilder, dx - verW / 2, dy - LINE + TEXT_PADDING_Y, dx + verW / 2, dy + LINE * 2 + TEXT_PADDING_Y, 400, BG, BG);
		fillGradient(matrix4f, bufferbuilder, dx - verW / 2 - preW, dy + TEXT_PADDING_Y, dx - verW / 2, dy + LINE + TEXT_PADDING_Y, 400, BG, BG);
		fillGradient(matrix4f, bufferbuilder, dx - horW * 3 / 2, dy + LINE * 2 + SPACING + TEXT_PADDING_Y, dx + horW * 3 / 2, dy + LINE * 3 + SPACING + TEXT_PADDING_Y, 400, BG, BG);

		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableDepthTest();
		BufferUploader.drawWithShader(bufferbuilder.end());
		RenderSystem.enableTexture();

		if (addC != null) gui.getFont().draw(poseStack, addC, dx - verW / 2f + TEXT_PADDING_X, dy - LINE, -1);
		gui.getFont().draw(poseStack, curC, dx - verW / 2f + TEXT_PADDING_X, dy, -1);
		if (subC != null) gui.getFont().draw(poseStack, subC, dx - verW / 2f + TEXT_PADDING_X, dy + LINE, -1);
		gui.getFont().draw(poseStack, preC, dx - verW / 2f - preW, dy, -1);

		dy += 2 * LINE + SPACING;

		gui.getFont().draw(poseStack, scaC, dx - scaW / 2f, dy, -1);
		if (lefC != null) gui.getFont().draw(poseStack, lefC, dx - lefW / 2f - horW, dy, -1);
		if (ritC != null) gui.getFont().draw(poseStack, ritC, dx - ritW / 2f + horW, dy, -1);


	}

	private static int width(ForgeGui gui, MutableComponent comp) {
		return gui.getFont().width(comp);
	}


}
