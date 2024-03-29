package dev.xkmc.lasertransport.content.capability.generic;

import dev.xkmc.lasertransport.content.capability.base.GenericNode;
import dev.xkmc.lasertransport.content.capability.base.SimpleNodeSupplier;
import dev.xkmc.lasertransport.content.flow.ErrorNode;
import dev.xkmc.lasertransport.content.flow.IContentHolder;
import dev.xkmc.lasertransport.content.flow.INodeSupplier;
import dev.xkmc.lasertransport.content.flow.TransportHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record NodalGenericHandler(IGenericNodeBlockEntity entity) implements GenericNode {

	@Override
	public boolean isValid(IContentHolder<GenericHolder> token) {
		return entity.getConfig().isContentValid(token.get());
	}

	@Override
	public List<INodeSupplier<GenericHolder>> getTargets() {
		Level level = entity.getThis().getLevel();
		if (level == null) return List.of();
		List<INodeSupplier<GenericHolder>> ans = new ArrayList<>();
		for (BlockPos pos : entity.getConnector().getAvailableTarget()) {
			BlockEntity target = level.getBlockEntity(pos);
			if (target != null) {
				var capType = entity.getCapType();
				var res = parseTarget(target, capType, pos);
				if (res != null) {
					ans.add(res);
					continue;
				}
			}
			ans.add(new SimpleNodeSupplier<>(pos, false, (ctx, token) -> new ErrorNode<>(pos)));
		}
		return ans;
	}

	@Nullable
	private <T> INodeSupplier<GenericHolder> parseTarget(BlockEntity target, ICapabilityEntry<T> capType, BlockPos pos) {
		var lazyCap = capType.cap().getHolder(entity, pos);
		if (lazyCap.resolve().isPresent()) {
			var cap = lazyCap.resolve().get();
			if (cap instanceof INodeHandlerWrapper wrapper) {
				var node = wrapper.node();
				return new SimpleNodeSupplier<>(pos, node.isReady(), (ctx, token) -> TransportHandler.broadcastRecursive(ctx, node, token));
			} else {
				return new SimpleNodeSupplier<>(pos, true, (ctx, token) -> new GenericNodeTarget(target, capType.parse(cap), token));
			}
		}
		return null;
	}

}
