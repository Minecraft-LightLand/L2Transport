package dev.xkmc.l2transport.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.xkmc.l2transport.init.L2Transport;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

@OnlyIn(Dist.CLIENT)
public class FluidRenderer extends RenderStateShard {

	private static final RenderType FLUID = RenderType.create(L2Transport.MODID + ":fluid",
			DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
					.setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER)
					.setTextureState(BLOCK_SHEET_MIPPED)
					.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
					.setLightmapState(LIGHTMAP)
					.setOverlayState(OVERLAY)
					.createCompositeState(true));

	public static VertexConsumer getFluidBuilder(MultiBufferSource buffer) {
		return buffer.getBuffer(FLUID);
	}

	public static void renderFluidBox(FluidStack fluidStack, AABB aabb, MultiBufferSource buffer, PoseStack ms, int light, boolean renderBottom) {
		VertexConsumer builder = getFluidBuilder(buffer);
		float xMin = (float) aabb.minX;
		float yMin = (float) aabb.minY;
		float zMin = (float) aabb.minZ;
		float xMax = (float) aabb.maxX;
		float yMax = (float) aabb.maxY;
		float zMax = (float) aabb.maxZ;
		FluidType type = fluidStack.getFluid().getFluidType();
		IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(fluidStack.getFluid().getFluidType());
		TextureAtlasSprite fluidTexture = Minecraft.getInstance()
				.getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
				.apply(props.getStillTexture(fluidStack));

		int color = props.getTintColor(fluidStack);
		int blockLightIn = (light >> 4) & 0xF;
		int luminosity = Math.max(blockLightIn, type.getLightLevel(fluidStack));
		light = (light & 0xF00000) | luminosity << 4;
		ms.pushPose();
		for (Direction side : Direction.values()) {
			if (side == Direction.DOWN && !renderBottom)
				continue;
			boolean positive = side.getAxisDirection() == AxisDirection.POSITIVE;
			if (side.getAxis()
					.isHorizontal()) {
				if (side.getAxis() == Axis.X) {
					renderStillTiledFace(side, zMin, yMin, zMax, yMax, positive ? xMax : xMin, builder, ms, light, color, fluidTexture);
				} else {
					renderStillTiledFace(side, xMin, yMin, xMax, yMax, positive ? zMax : zMin, builder, ms, light, color, fluidTexture);
				}
			} else {
				renderStillTiledFace(side, xMin, zMin, xMax, zMax, positive ? yMax : yMin, builder, ms, light, color, fluidTexture);
			}
		}

		ms.popPose();
	}

	public static void renderStillTiledFace(Direction dir, float left, float down, float right, float up,
											float depth, VertexConsumer builder, PoseStack ms, int light, int color, TextureAtlasSprite texture) {
		FluidRenderer.renderTiledFace(dir, left, down, right, up, depth, builder, ms, light, color, texture, 1);
	}

	public static void renderTiledFace(Direction dir, float left, float down, float right, float up,
									   float depth, VertexConsumer builder, PoseStack ms, int light, int color, TextureAtlasSprite texture,
									   float textureScale) {
		boolean positive = dir.getAxisDirection() == Direction.AxisDirection.POSITIVE;
		boolean horizontal = dir.getAxis().isHorizontal();
		boolean x = dir.getAxis() == Axis.X;

		float shrink = texture.uvShrinkRatio() * 0.25f * textureScale;
		float centerU = texture.getU0() + (texture.getU1() - texture.getU0()) * 0.5f * textureScale;
		float centerV = texture.getV0() + (texture.getV1() - texture.getV0()) * 0.5f * textureScale;

		float f;
		float x2 = 0;
		float y2 = 0;
		float u1, u2;
		float v1, v2;
		for (float x1 = left; x1 < right; x1 = x2) {
			f = Mth.floor(x1);
			x2 = Math.min(f + 1, right);
			if (dir == Direction.NORTH || dir == Direction.EAST) {
				f = Mth.ceil(x2);
				u1 = texture.getU((f - x2) * 16 * textureScale);
				u2 = texture.getU((f - x1) * 16 * textureScale);
			} else {
				u1 = texture.getU((x1 - f) * 16 * textureScale);
				u2 = texture.getU((x2 - f) * 16 * textureScale);
			}
			u1 = Mth.lerp(shrink, u1, centerU);
			u2 = Mth.lerp(shrink, u2, centerU);
			for (float y1 = down; y1 < up; y1 = y2) {
				f = Mth.floor(y1);
				y2 = Math.min(f + 1, up);
				if (dir == Direction.UP) {
					v1 = texture.getV((y1 - f) * 16 * textureScale);
					v2 = texture.getV((y2 - f) * 16 * textureScale);
				} else {
					f = Mth.ceil(y2);
					v1 = texture.getV((f - y2) * 16 * textureScale);
					v2 = texture.getV((f - y1) * 16 * textureScale);
				}
				v1 = Mth.lerp(shrink, v1, centerV);
				v2 = Mth.lerp(shrink, v2, centerV);

				if (horizontal) {
					if (x) {
						putVertex(builder, ms, depth, y2, positive ? x2 : x1, color, u1, v1, dir, light);
						putVertex(builder, ms, depth, y1, positive ? x2 : x1, color, u1, v2, dir, light);
						putVertex(builder, ms, depth, y1, positive ? x1 : x2, color, u2, v2, dir, light);
						putVertex(builder, ms, depth, y2, positive ? x1 : x2, color, u2, v1, dir, light);
					} else {
						putVertex(builder, ms, positive ? x1 : x2, y2, depth, color, u1, v1, dir, light);
						putVertex(builder, ms, positive ? x1 : x2, y1, depth, color, u1, v2, dir, light);
						putVertex(builder, ms, positive ? x2 : x1, y1, depth, color, u2, v2, dir, light);
						putVertex(builder, ms, positive ? x2 : x1, y2, depth, color, u2, v1, dir, light);
					}
				} else {
					putVertex(builder, ms, x1, depth, positive ? y1 : y2, color, u1, v1, dir, light);
					putVertex(builder, ms, x1, depth, positive ? y2 : y1, color, u1, v2, dir, light);
					putVertex(builder, ms, x2, depth, positive ? y2 : y1, color, u2, v2, dir, light);
					putVertex(builder, ms, x2, depth, positive ? y1 : y2, color, u2, v1, dir, light);
				}
			}
		}
	}

	private static void putVertex(VertexConsumer builder, PoseStack ms, float x, float y, float z, int color, float u,
								  float v, Direction face, int light) {

		Vec3i normal = face.getNormal();
		Pose peek = ms.last();
		int a = color >> 24 & 0xff;
		int r = color >> 16 & 0xff;
		int g = color >> 8 & 0xff;
		int b = color & 0xff;

		builder.vertex(peek.pose(), x, y, z)
				.color(r, g, b, a)
				.uv(u, v)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(light)
				.normal(peek.normal(), normal.getX(), normal.getY(), normal.getZ())
				.endVertex();
	}

	public FluidRenderer(String p_110161_, Runnable p_110162_, Runnable p_110163_) {
		super(p_110161_, p_110162_, p_110163_);
	}

}