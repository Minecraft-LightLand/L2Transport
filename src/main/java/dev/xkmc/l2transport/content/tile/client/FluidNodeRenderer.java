package dev.xkmc.l2transport.content.tile.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2transport.content.tile.base.IRenderableFluidNode;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FluidNodeRenderer<T extends BlockEntity & IRenderableFluidNode> extends NodeRenderer<T> {

	public FluidNodeRenderer(BlockEntityRendererProvider.Context dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(T entity, float partialTick, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
		super.render(entity, partialTick, poseStack, source, light, overlay);
		Level level = entity.getLevel();
		if (level != null && !entity.getFluid().isEmpty()) {
			//TODO render fluid
			//RenderUtils.renderItemAbove(entity.getItem(), 0.5, level, partialTick, poseStack, source, light, overlay);
		}
	}
}
