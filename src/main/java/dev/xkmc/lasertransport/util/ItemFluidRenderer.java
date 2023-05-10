package dev.xkmc.lasertransport.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Matrix4f;

public class ItemFluidRenderer {

	private static final int TEXTURE_SIZE = 16;

	public static final ItemFluidRenderer RENDERER = new ItemFluidRenderer(TEXTURE_SIZE, TEXTURE_SIZE);

	private final int width;
	private final int height;

	private ItemFluidRenderer(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void render(PoseStack poseStack, FluidStack fluidStack) {
		RenderSystem.enableBlend();

		drawFluid(poseStack, width, height, fluidStack);

		RenderSystem.setShaderColor(1, 1, 1, 1);

		RenderSystem.disableBlend();
	}

	private TextureAtlasSprite getStillFluidSprite(FluidStack fluidStack) {
		Fluid fluid = fluidStack.getFluid();
		IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
		ResourceLocation fluidStill = renderProperties.getStillTexture(fluidStack);
		Minecraft minecraft = Minecraft.getInstance();
		return minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
	}

	private int getColorTint(FluidStack ingredient) {
		Fluid fluid = ingredient.getFluid();
		IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
		return renderProperties.getTintColor(ingredient);
	}

	private void drawFluid(PoseStack poseStack, final int width, final int height, FluidStack fluidStack) {
		Fluid fluid = fluidStack.getFluid();
		if (fluid.isSame(Fluids.EMPTY)) {
			return;
		}
		TextureAtlasSprite fluidStillSprite = getStillFluidSprite(fluidStack);
		int fluidColor = getColorTint(fluidStack);
		drawTiledSprite(poseStack, width, height, fluidColor, height, fluidStillSprite);
	}

	private static void drawTiledSprite(PoseStack poseStack, final int tiledWidth, final int tiledHeight, int color, long scaledAmount, TextureAtlasSprite sprite) {
		RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
		Matrix4f matrix = poseStack.last().pose();
		setGLColorFromInt(color);

		final int xTileCount = tiledWidth / TEXTURE_SIZE;
		final int xRemainder = tiledWidth - (xTileCount * TEXTURE_SIZE);
		final long yTileCount = scaledAmount / TEXTURE_SIZE;
		final long yRemainder = scaledAmount - (yTileCount * TEXTURE_SIZE);

		for (int xTile = 0; xTile <= xTileCount; xTile++) {
			for (int yTile = 0; yTile <= yTileCount; yTile++) {
				int width = (xTile == xTileCount) ? xRemainder : TEXTURE_SIZE;
				long height = (yTile == yTileCount) ? yRemainder : TEXTURE_SIZE;
				int x = (xTile * TEXTURE_SIZE);
				int y = tiledHeight - ((yTile + 1) * TEXTURE_SIZE);
				if (width > 0 && height > 0) {
					long maskTop = TEXTURE_SIZE - height;
					int maskRight = TEXTURE_SIZE - width;

					drawTextureWithMasking(matrix, x, y, sprite, maskTop, maskRight, 100);
				}
			}
		}
	}

	private static void setGLColorFromInt(int color) {
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;
		float alpha = ((color >> 24) & 0xFF) / 255F;
		RenderSystem.setShaderColor(red, green, blue, alpha);
	}

	private static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, long maskTop, long maskRight, float zLevel) {
		float uMin = textureSprite.getU0();
		float uMax = textureSprite.getU1();
		float vMin = textureSprite.getV0();
		float vMax = textureSprite.getV1();
		uMax = uMax - (maskRight / 16F * (uMax - uMin));
		vMax = vMax - (maskTop / 16F * (vMax - vMin));

		RenderSystem.setShader(GameRenderer::getPositionTexShader);

		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.vertex(matrix, xCoord, yCoord + 16, zLevel).uv(uMin, vMax).endVertex();
		bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + 16, zLevel).uv(uMax, vMax).endVertex();
		bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + maskTop, zLevel).uv(uMax, vMin).endVertex();
		bufferBuilder.vertex(matrix, xCoord, yCoord + maskTop, zLevel).uv(uMin, vMin).endVertex();
		tessellator.end();
	}

}
