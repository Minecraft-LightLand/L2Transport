package dev.xkmc.lasertransport.content.capability.item;

import dev.xkmc.lasertransport.content.flow.IContentToken;
import dev.xkmc.lasertransport.content.flow.INetworkNode;
import dev.xkmc.lasertransport.content.flow.RealToken;
import dev.xkmc.lasertransport.content.flow.TransportContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class LevelBlockTarget implements INetworkNode<ItemStack> {

	private final Level level;
	private final BlockPos pos;
	private final IContentToken<ItemStack> stack;
	private final boolean consume;

	public LevelBlockTarget(Level level, BlockPos pos, IContentToken<ItemStack> stack) {
		this.level = level;
		this.pos = pos;
		this.stack = stack;
		boolean consume = false;
		if (stack.getAvailable() > 0 && stack.get().get().getItem() instanceof BlockItem bi) {
			BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false);
			BlockPlaceContext ctx = new BlockPlaceContext(level, null, InteractionHand.MAIN_HAND, stack.get().get(), hit);
			if (level.getBlockState(pos).canBeReplaced(ctx)) {
				BlockState state = bi.getBlock().getStateForPlacement(ctx);
				if (state != null) {
					consume = true;
					stack.consume(1);
				}
			}
		}
		this.consume = consume;
	}

	@Override
	public long getConsumed() {
		return consume ? 1 : 0;
	}

	@Override
	public void refreshCoolDown(TransportContext<ItemStack> ctx, boolean success) {

	}

	@Override
	public void perform(RealToken<ItemStack> token) {
		if (!consume) return;
		ItemStack item = token.split(1);
		if (stack.get().get().getItem() instanceof BlockItem bi) {
			BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false);
			BlockPlaceContext ctx = new BlockPlaceContext(level, null, InteractionHand.MAIN_HAND, item, hit);
			if (level.getBlockState(pos).canBeReplaced(ctx)) {
				BlockState state = bi.getBlock().getStateForPlacement(ctx);
				if (state != null) {
					level.setBlockAndUpdate(pos, state);
					return;
				}
			}
		}
		token.gain(1);
	}

	@Override
	public boolean hasAction() {
		return consume;
	}

	@Override
	public BlockPos getIdentifier() {
		return pos;
	}
}
