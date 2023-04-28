package dev.xkmc.lasertransport.content.client.node;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.lasertransport.content.items.tools.ILinker;
import dev.xkmc.lasertransport.content.items.tools.LinkerItem;
import dev.xkmc.lasertransport.content.tile.base.CoolDownType;
import dev.xkmc.lasertransport.content.tile.base.IRenderableConnector;
import dev.xkmc.lasertransport.content.tile.base.IRenderableNode;
import dev.xkmc.lasertransport.init.data.LTModConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class NodeRenderer<T extends BlockEntity & IRenderableNode> implements BlockEntityRenderer<T> {

	public static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("textures/entity/beacon_beam.png");

	public static void renderLightBeam(PoseStack mat, MultiBufferSource vcp, ResourceLocation id, float t1,
									   BeamRenderer br, double x, double y, double z) {
		double xz = Math.sqrt(x * x + z * z);
		float len = (float) Math.sqrt(xz * xz + y * y);
		mat.mulPose(Vector3f.YN.rotation((float) (Math.atan2(z, x) - Math.PI / 2)));
		mat.mulPose(Vector3f.XN.rotation((float) (Math.atan2(y, xz))));
		mat.mulPose(Vector3f.ZP.rotationDegrees(t1 * 4.5f));
		float t2 = t1 * 0.5f;
		float h = 0.05f;
		br.setUV(0, 1, t2, t2 + len / h);
		br.drawCube(mat, vcp.getBuffer(RenderType.beaconBeam(id, false)), 0, len, h);
	}

	public NodeRenderer(BlockEntityRendererProvider.Context dispatcher) {
	}

	@Override
	public void render(T entity, float partialTick, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
		NodeRenderType type = RenderManager.getRenderConfig(entity);
		if (type.renderLinks()) {
			renderLinks(entity, partialTick, poseStack, source, light, overlay);
		}
	}

	private void renderLinks(T entity, float partialTick, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
		Level level = entity.getLevel();
		long gameTime = level == null ? 0 : level.getGameTime();
		float time = Math.floorMod(gameTime, 80L) + partialTick;
		poseStack.pushPose();
		poseStack.translate(0.5D, 0.5D, 0.5D);
		BeamRenderer br = new BeamRenderer();
		IRenderableConnector connector = entity.getConnector();
		for (BlockPos target : connector.getVisibleConnection()) {
			float coolDown = Math.max(0, connector.getCoolDown(target) - partialTick);
			int max = Math.max(1, connector.getMaxCoolDown(target));
			float percentage = Mth.clamp(coolDown / max, 0, 1);
			CoolDownType type = connector.getType(target);
			if (!entity.isTargetValid(target)) {
				type = type.invalidate();
			}
			type.setColor(percentage, br::setColorHSB);

			BlockPos p = target.subtract(entity.getBlockPos());
			int x = p.getX();
			int y = p.getY();
			int z = p.getZ();

			poseStack.pushPose();
			if (type.isReversed()) {
				poseStack.translate(x, y, z);
				x *= -1;
				y *= -1;
				z *= -1;
			}
			renderLightBeam(poseStack, source, BEAM_TEXTURE, time, br, x, y, z);
			poseStack.popPose();
		}
		ItemStack linker = Proxy.getPlayer().getMainHandItem();
		if (linker.getItem() instanceof ILinker item && item.storesPos()) {
			LinkerItem.LinkData pos = LinkerItem.getData(linker);
			if (pos != null && pos.pos().equals(entity.getBlockPos())) {
				br.setColorHSB(0, 0, 0.5f);
				Vec3 p = Proxy.getPlayer().getEyePosition(partialTick);
				BlockPos c = entity.getBlockPos();
				renderLightBeam(poseStack, source, BEAM_TEXTURE, time, br, p.x - c.getX() - 0.5, p.y - c.getY() - 1, p.z - c.getZ() - 0.5);
			}
		}
		poseStack.popPose();
	}

	public boolean shouldRenderOffScreen(T entity) {
		return RenderManager.getRenderConfig(entity).renderLinks();
	}

	@Override
	public int getViewDistance() {
		return LTModConfig.CLIENT.renderRange.get();
	}

}
