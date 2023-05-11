package dev.xkmc.lasertransport.content.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.lasertransport.content.craft.logic.CraftGrid;
import dev.xkmc.lasertransport.content.craft.logic.CraftSlotType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

public record ClientCraftTooltip(CraftGrid items) implements ClientTooltipComponent {

	public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/gui/container/bundle.png");

	@Override
	public int getHeight() {
		return items.height() * 18;
	}

	@Override
	public int getWidth(Font font) {
		return items.width() * 18;
	}

	@Override
	public void renderText(Font pFont, int pX, int pY, Matrix4f pMatrix4f, MultiBufferSource.BufferSource pBufferSource) {

	}

	@Override
	public void renderImage(Font font, int mouseX, int mouseY, PoseStack poseStack, ItemRenderer pItemRenderer) {
		for (int i = 0; i < items.height(); i++) {
			for (int j = 0; j < items.width(); j++) {
				int x = mouseX + j * 18;
				int y = mouseY + i * 18;
				var opt = this.items.list()[i][j];
				if (opt.type() == CraftSlotType.EMPTY) continue;
				if (opt.type() == CraftSlotType.RESULT) {
					this.blit(poseStack, x, y, 0, Texture.BLOCKED_SLOT);
					continue;
				}
				this.blit(poseStack, x, y, 0, Texture.SLOT);
				ItemStack itemstack = opt.stack();
				if (itemstack.isEmpty()) continue;
				pItemRenderer.renderAndDecorateItem(poseStack, itemstack, x + 1, y + 1, i);
				pItemRenderer.renderGuiItemDecorations(poseStack, font, itemstack, x + 1, y + 1);
			}
		}
	}

	private void blit(PoseStack poseStack, int x, int y, int z, Texture texture) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
		GuiComponent.blit(poseStack, x, y, z, texture.x, texture.y, texture.w, texture.h, 128, 128);
	}

	@OnlyIn(Dist.CLIENT)
	enum Texture {
		SLOT(0, 0, 18, 18),
		BLOCKED_SLOT(0, 40, 18, 18);

		public final int x;
		public final int y;
		public final int w;
		public final int h;

		Texture(int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
	}


}
