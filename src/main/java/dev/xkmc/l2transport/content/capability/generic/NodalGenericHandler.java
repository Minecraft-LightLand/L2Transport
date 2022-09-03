package dev.xkmc.l2transport.content.capability.generic;

import dev.xkmc.l2transport.content.capability.base.GenericNode;
import dev.xkmc.l2transport.content.capability.base.SimpleNodeSupplier;
import dev.xkmc.l2transport.content.flow.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record NodalGenericHandler(IGenericNodeBlockEntity be) implements GenericNode {

	@Override
	public NetworkType getNetworkType() {
		return be.getConnector();
	}

	@Override
	public boolean isValid(IContentHolder<GenericHolder> token) {
		return be.isContentValid(token.get());
	}

	@Override
	public void refreshCoolDown(BlockPos target, boolean success, TransportContext<GenericHolder> ctx) {
		be.refreshCoolDown(target, success, ctx.simulate);
	}

	@Override
	public BlockPos getIdentifier() {
		return be.getBlockPos();
	}

	@Override
	public boolean isReady() {
		return be.getConnector().isReady();
	}

	@Override
	public List<INodeSupplier<GenericHolder>> getTargets() {
		Level level = be.getLevel();
		if (level == null) return List.of();
		List<INodeSupplier<GenericHolder>> ans = new ArrayList<>();
		for (BlockPos pos : be.getConnector().getAvailableTarget()) {
			BlockEntity target = level.getBlockEntity(pos);
			if (target != null) {
				INodeSupplier<GenericHolder> first = null;
				for (var capType : GenericCapabilityHandler.CAPABILITIES.values()) {
					var res = parseTarget(target, capType, pos);
					if (res != null) {
						first = res;
						break;
					}
				}
				if (first != null) {
					ans.add(first);
					continue;
				}
			}
			ans.add(new SimpleNodeSupplier<>(pos, false, (ctx, token) -> new ErrorNode<>(pos)));
		}
		return ans;
	}

	@Nullable
	private <T> INodeSupplier<GenericHolder> parseTarget(BlockEntity target, CapabilityEntry<T> capType, BlockPos pos) {
		var lazyCap = target.getCapability(capType.cap());
		if (lazyCap.resolve().isPresent()) {
			var cap = lazyCap.resolve().get();
			if (cap instanceof GenericNode node) {
				return new SimpleNodeSupplier<>(pos, node.isReady(), (ctx, token) -> TransportHandler.broadcastRecursive(ctx, node, token));
			} else {
				return new SimpleNodeSupplier<>(pos, true, (ctx, token) -> new GenericNodeTarget(target, capType.parser().apply(cap), token));
			}
		}
		return null;
	}

}
