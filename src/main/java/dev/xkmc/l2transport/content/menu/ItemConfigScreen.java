package dev.xkmc.l2transport.content.menu;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.base.menu.BaseContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ItemConfigScreen extends BaseContainerScreen<ItemConfigMenu> {

	public ItemConfigScreen(ItemConfigMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

	@Override
	protected void renderBg(PoseStack poseStack, float ptick, int mx, int my) {

	}

}
