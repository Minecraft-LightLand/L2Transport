package dev.xkmc.l2transport.content.menu;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

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

	protected boolean click(int btn) {
		if (this.menu.clickMenuButton(Proxy.getClientPlayer(), btn)) {
			Minecraft.getInstance().gameMode.handleInventoryButtonClick(this.menu.containerId, btn);
			return true;
		} else {
			return false;
		}
	}

}
