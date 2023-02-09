package dev.xkmc.l2transport.content.client.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2library.base.overlay.SelectionSideBar;
import dev.xkmc.l2library.base.overlay.TextBox;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2transport.content.tools.ILinker;
import dev.xkmc.l2transport.content.tools.ToolSelectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import java.util.List;

public class ToolSelection extends SelectionSideBar {

	public static final ToolSelection INSTANCE = new ToolSelection();

	private ToolSelection() {
		super(40, 3);
	}

	@Override
	public Pair<List<ItemStack>, Integer> getItems() {
		return Pair.of(ToolSelectionHelper.LIST, ToolSelectionHelper.getIndex(Proxy.getClientPlayer()));
	}

	@Override
	public boolean isAvailable(ItemStack itemStack) {
		return true;
	}

	@Override
	public boolean onCenter() {
		return false;
	}

	@Override
	public int getSignature() {
		LocalPlayer player = Proxy.getClientPlayer();
		if (player == null) return 0;
		return ToolSelectionHelper.getIndex(player);
	}

	@Override
	public boolean isScreenOn() {
		if (Minecraft.getInstance().screen != null) return false;
		LocalPlayer player = Proxy.getClientPlayer();
		if (player == null) return false;
		//if (!player.isShiftKeyDown()) return false;
		return player.getMainHandItem().getItem() instanceof ILinker l1 && l1.toolSelect() ||
				player.getOffhandItem().getItem() instanceof ILinker l2 && l2.toolSelect();
	}

	public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		//TODO move to library
		if (this.ease((float) gui.getGuiTicks() + partialTick)) {
			this.initRender();
			gui.setupOverlayRenderState(true, false);
			Pair<List<ItemStack>, Integer> content = this.getItems();
			List<ItemStack> list = content.getFirst();
			int selected = content.getSecond();
			ItemRenderer renderer = gui.getMinecraft().getItemRenderer();
			Font font = gui.getMinecraft().font;
			int dx = this.getXOffset(width);
			int dy = this.getYOffset(height);
			for (int i = 0; i < list.size(); ++i) {
				ItemStack stack = list.get(i);
				int y = 18 * i + dy;
				this.renderSelection(dx, y, 64, this.isAvailable(stack), selected == i);
				if (this.ease_time == this.max_ease) {
					TextBox box = new TextBox(width, height, 0, 1, dx + 22, y + 8, -1);
					box.renderLongText(gui, poseStack, List.of(stack.getHoverName()));
				}
				if (!stack.isEmpty()) {
					renderer.renderAndDecorateItem(stack, dx, y);
					renderer.renderGuiItemDecorations(font, stack, dx, y);
				}
			}

		}
	}

	protected int getXOffset(int width) {
		float progress = (this.max_ease - this.ease_time) / this.max_ease;
		return this.onCenter() ? width / 2 - 54 - 1 - Math.round(progress * (float) width / 2.0F) : 2 - Math.round(progress * 20.0F);
	}

	protected int getYOffset(int height) {
		return height / 2 - 9 * ToolSelectionHelper.LIST.size() + 1;
	}

}
