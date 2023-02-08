package dev.xkmc.l2transport.content.capability.base;

import dev.xkmc.l2transport.content.flow.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public interface BaseNodeHolder<T, R> extends EntityNodeHolder<T> {

	@Nullable
	INodeHolder<T> castNode(R cap);

	Capability<R> getCapability();

	INetworkNode<T> createLeaf(BlockEntity target, R cap, IContentToken<T> token);

	@Override
	default List<INodeSupplier<T>> getTargets() {
		INodeBlockEntity be = entity();
		Level level = be.getThis().getLevel();
		if (level == null) return List.of();
		List<INodeSupplier<T>> ans = new ArrayList<>();
		for (BlockPos pos : be.getConnector().getAvailableTarget()) {
			BlockEntity target = level.getBlockEntity(pos);
			if (target != null) {
				var lazyCap = be.getCapability(getCapability(), pos);
				if (lazyCap.resolve().isPresent()) {
					var cap = lazyCap.resolve().get();
					var node = castNode(cap);
					if (node != null) {
						ans.add(new SimpleNodeSupplier<>(pos, node.isReady(), (ctx, token) -> TransportHandler.broadcastRecursive(ctx, node, token)));
					} else {
						ans.add(new SimpleNodeSupplier<>(pos, true, (ctx, token) -> createLeaf(target, cap, token)));
					}
					continue;
				}
			}
			ans.add(new SimpleNodeSupplier<>(pos, true, (ctx, token) -> getWorldNode(pos, token)));
		}
		return ans;
	}

	default INetworkNode<T> getWorldNode(BlockPos pos, IContentToken<T> token) {
		return new ErrorNode<>(pos);
	}

}
