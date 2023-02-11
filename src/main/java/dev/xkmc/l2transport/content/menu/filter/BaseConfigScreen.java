package dev.xkmc.l2transport.content.menu.filter;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2transport.content.configurables.ToggleConfig;
import dev.xkmc.l2transport.content.menu.ghost.ItemTarget;
import dev.xkmc.l2transport.init.L2Transport;
import dev.xkmc.l2transport.init.data.LangData;
import dev.xkmc.l2transport.network.SetItemFilterToServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseConfigScreen<T extends BaseConfigMenu<T>> extends AbstractContainerScreen<T> {

	public BaseConfigScreen(T cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
		this.imageHeight = this.menu.sprite.getHeight();
		this.inventoryLabelY = this.menu.sprite.getPlInvY() - 11;
	}

	public void render(PoseStack stack, int mx, int my, float partial) {
		super.render(stack, mx, my, partial);
		this.renderTooltip(stack, mx, my);
	}

	@Override
	protected void renderBg(PoseStack poseStack, float ptick, int mx, int my) {
		var sr = menu.sprite.getRenderer(this);
		sr.start(poseStack);
		ToggleConfig config = menu.getConfig().getToggleConfig();
		if (config.isBlacklist()) sr.draw(poseStack, "filter", "filter_on");
		if (config.isTagMatch()) sr.draw(poseStack, "match", "match_on");
		if (config.isLocked()) sr.draw(poseStack, "lock", "lock_on");
	}

	@Override
	public boolean mouseClicked(double mx, double my, int btn) {
		if (menu.sprite.within("filter", mx - leftPos, my - topPos)) {
			click(0);
			return true;
		}
		if (menu.sprite.within("match", mx - leftPos, my - topPos)) {
			click(1);
			return true;
		}
		if (menu.sprite.within("lock", mx - leftPos, my - topPos)) {
			click(2);
			return true;
		}
		return super.mouseClicked(mx, my, btn);
	}

	@Override
	protected void renderTooltip(PoseStack poseStack, int mx, int my) {
		if (menu.getCarried().isEmpty()) {
			ToggleConfig c = menu.getConfig().getToggleConfig();
			if (menu.sprite.within("filter", mx - leftPos, my - topPos)) {
				renderTooltip(poseStack, (c.isBlacklist() ? LangData.UI_BLACKLIST : LangData.UI_WHITELIST).get(), mx, my);
			}
			if (menu.sprite.within("match", mx - leftPos, my - topPos)) {
				renderTooltip(poseStack, (c.isTagMatch() ? LangData.UI_MATCH_TAG : LangData.UI_MATCH_ITEM).get(), mx, my);
			}
			if (menu.sprite.within("lock", mx - leftPos, my - topPos)) {
				renderTooltip(poseStack, (c.isLocked() ? LangData.UI_LOCKED : LangData.UI_UNLOCKED).get(), mx, my);
			}
		}
		super.renderTooltip(poseStack, mx, my);
	}

	protected boolean click(int btn) {
		if (this.menu.clickMenuButton(Proxy.getClientPlayer(), btn)) {
			assert Minecraft.getInstance().gameMode != null;
			Minecraft.getInstance().gameMode.handleInventoryButtonClick(this.menu.containerId, btn);
			return true;
		} else {
			return false;
		}
	}

	public List<ItemTarget> getTargets() {
		List<ItemTarget> ans = new ArrayList<>();
		var rect = menu.sprite.getComp("grid");
		for (int y = 0; y < rect.ry; y++) {
			for (int x = 0; x < rect.rx; x++) {
				int id = y * rect.rx + x;
				ans.add(new ItemTarget(rect.x + x * rect.w + leftPos, rect.y + y * rect.h + topPos, 16, 16, stack -> addGhost(id, stack)));
			}
		}
		return ans;
	}

	public void addGhost(int ind, ItemStack stack) {
		menu.setSlotContent(ind, stack);
		L2Transport.HANDLER.toServer(new SetItemFilterToServer(ind, stack));
	}

}
