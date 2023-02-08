package dev.xkmc.l2transport.content.capability.item;

import dev.xkmc.l2transport.content.flow.IContentToken;
import dev.xkmc.l2transport.content.flow.INetworkNode;
import dev.xkmc.l2transport.content.flow.RealToken;
import dev.xkmc.l2transport.content.flow.TransportContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ComposterTarget implements INetworkNode<ItemStack> {

	private final Level level;
	private final BlockPos pos;
	private final int consumed;

	public ComposterTarget(Level level, BlockPos pos, IContentToken<ItemStack> stack) {
		this.level = level;
		this.pos = pos;
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() == Blocks.COMPOSTER) {
			var c = ((ComposterBlock) state.getBlock()).getContainer(state, level, pos);
			consumed = c.canPlaceItem(0, stack.get().get()) ? 1 : 0;
		} else {
			consumed = 0;
		}
	}

	@Override
	public long getConsumed() {
		return consumed;
	}

	@Override
	public void refreshCoolDown(TransportContext<ItemStack> ctx, boolean success) {

	}

	@Override
	public void perform(RealToken<ItemStack> token) {
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() == Blocks.COMPOSTER) {
			var c = ((ComposterBlock) state.getBlock()).getContainer(state, level, pos);
			ItemStack item = token.split(1);
			c.setItem(0, item);
			if (item.getCount() > 0) {
				token.gain(item.getCount());
			}
		}
	}

	@Override
	public boolean hasAction() {
		return consumed > 0;
	}

	@Override
	public BlockPos getIdentifier() {
		return pos;
	}
}
