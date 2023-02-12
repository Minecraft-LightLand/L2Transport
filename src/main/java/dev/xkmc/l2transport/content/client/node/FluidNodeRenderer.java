package dev.xkmc.l2transport.content.client.node;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2transport.content.tile.base.IRenderableFluidNode;
import dev.xkmc.l2transport.util.BlockFluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FluidNodeRenderer<T extends BlockEntity & IRenderableFluidNode> extends NodeRenderer<T> {

	public FluidNodeRenderer(BlockEntityRendererProvider.Context dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(T entity, float partialTick, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
		super.render(entity, partialTick, poseStack, source, light, overlay);
		Level level = entity.getLevel();
		if (RenderManager.getRenderConfig(entity).renderFilters() && level != null && !entity.getFluid().isEmpty()) {
			VoxelShape shape = entity.getBlockState().getShape(level, entity.getBlockPos());
			BlockFluidRenderer.renderFluidBox(entity.getFluid(), shape.bounds().deflate(1e-3), source, poseStack, light, true);
		}
	}
}
