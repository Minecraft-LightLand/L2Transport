package dev.xkmc.l2transport.content.capability.item;

import dev.xkmc.l2transport.content.flow.IContentToken;
import dev.xkmc.l2transport.content.flow.INetworkNode;
import dev.xkmc.l2transport.content.flow.RealToken;
import dev.xkmc.l2transport.content.flow.TransportContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LevelItemTarget implements INetworkNode<ItemStack> {

	private final Level level;
	private final BlockPos pos;
	private final IContentToken<ItemStack> stack;

	public LevelItemTarget(Level level, BlockPos pos, IContentToken<ItemStack> stack) {
		this.level = level;
		this.pos = pos;
		this.stack = stack;
	}

	@Override
	public long getConsumed() {
		return Math.min(stack.getAvailable(), stack.get().get().getMaxStackSize());
	}

	@Override
	public void refreshCoolDown(TransportContext<ItemStack> ctx, boolean success) {

	}

	@Override
	public void perform(RealToken<ItemStack> token) {
		ItemStack item = token.split(getConsumed());
		level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, item));
	}

	@Override
	public boolean hasAction() {
		return getConsumed() > 0;
	}

	@Override
	public BlockPos getIdentifier() {
		return pos;
	}
}
