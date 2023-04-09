package dev.xkmc.lasertransport.content.client.node;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.util.math.RenderUtils;
import dev.xkmc.lasertransport.content.craft.tile.ItemHolderBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CraftNodeRenderer<T extends ItemHolderBlockEntity> implements BlockEntityRenderer<T> {

	public CraftNodeRenderer(BlockEntityRendererProvider.Context dispatcher) {

	}

	@Override
	public void render(T entity, float partialTick, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
		Level level = entity.getLevel();
		if (level == null) return;
		ItemStack stack = entity.items.getStackInSlot(0);
		if (stack.isEmpty()) return;
		RenderUtils.renderItemAbove(stack, 0.5, level, partialTick, poseStack, source, light, overlay);
	}
}
