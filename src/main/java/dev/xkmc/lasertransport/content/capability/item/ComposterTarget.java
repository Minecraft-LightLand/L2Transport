package dev.xkmc.lasertransport.content.capability.item;

import dev.xkmc.lasertransport.content.flow.IContentToken;
import dev.xkmc.lasertransport.content.flow.INetworkNode;
import dev.xkmc.lasertransport.content.flow.RealToken;
import dev.xkmc.lasertransport.content.flow.TransportContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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
		if (stack.getAvailable() > 0 && state.getBlock() == Blocks.COMPOSTER) {
			consumed = ComposterBlock.COMPOSTABLES.containsKey(stack.get().get().getItem()) &&
					state.getValue(ComposterBlock.LEVEL) < 7 ? 1 : 0;
		} else {
			consumed = 0;
		}
		stack.consume(consumed);
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
			BlockState next = ComposterBlock.insertItem(state, (ServerLevel) level, item, pos);
			if (next != state) {
				level.setBlockAndUpdate(pos, next);
			}
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
