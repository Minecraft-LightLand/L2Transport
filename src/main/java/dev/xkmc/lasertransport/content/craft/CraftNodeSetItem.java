package dev.xkmc.lasertransport.content.craft;

import dev.xkmc.l2library.block.mult.OnClickBlockMethod;
import dev.xkmc.lasertransport.content.items.tools.ILinker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CraftNodeSetItem implements OnClickBlockMethod {

	@Override
	public InteractionResult onClick(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		ItemStack hand = player.getItemInHand(interactionHand);
		if (hand.getItem() instanceof ILinker)
			return InteractionResult.PASS;
		BlockEntity be = level.getBlockEntity(blockPos);
		if (be instanceof ItemHolderBlockEntity node) {
			ItemStack stack = node.items.getStackInSlot(0);
			if (stack.isEmpty()) {
				if (!hand.isEmpty()) {
					if (!level.isClientSide()) {
						ItemStack copy = hand.copy();
						hand.setCount(node.items.insertItem(0, copy, false).getCount());
					}
					return InteractionResult.SUCCESS;
				}
			} else {
				if (!level.isClientSide()) {
					ItemStack extracted = node.items.extractItem(0, 64, false);
					player.getInventory().placeItemBackInInventory(extracted);
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

}
