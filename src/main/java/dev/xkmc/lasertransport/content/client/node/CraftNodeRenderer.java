package dev.xkmc.lasertransport.content.client.node;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.util.math.RenderUtils;
import dev.xkmc.lasertransport.content.craft.tile.ItemHolderBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class CraftNodeRenderer<T extends ItemHolderBlockEntity> implements BlockEntityRenderer<T> {

	public CraftNodeRenderer(BlockEntityRendererProvider.Context dispatcher) {

	}

	@Override
	public void render(T entity, float partialTick, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
		Level level = entity.getLevel();
		if (level == null) return;
		List<ItemStack> list = entity.getHolder().getAll();
		int edge = (int) Math.ceil(Math.pow(list.size(), 1 / 3d));
		int ind = 0;
		for (int i = 0; i < edge; i++) {
			for (int j = 0; j < edge; j++) {
				for (int k = 0; k < edge; k++) {
					ItemStack stack = list.get(ind);
					ind++;
					if (stack.isEmpty()) continue;
					poseStack.pushPose();
					poseStack.translate((k + 0.5) / edge, (i + 0.5) / edge, (j + 0.5) / edge);
					RenderUtils.renderItemAbove(stack, 0.5, level, partialTick, poseStack, source, light, overlay);
					poseStack.popPose();
					if (ind >= list.size()) return;
				}
			}
		}
	}
}
