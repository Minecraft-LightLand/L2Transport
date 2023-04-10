package dev.xkmc.lasertransport.content.craft.block;

import dev.xkmc.l2library.block.mult.OnClickBlockMethod;
import dev.xkmc.lasertransport.content.craft.tile.IItemHolderNode;
import dev.xkmc.lasertransport.content.items.tools.ILinker;
import dev.xkmc.lasertransport.content.tile.base.NodeBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ItemHolderTakeItem implements OnClickBlockMethod {

	@Override
	public InteractionResult onClick(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		ItemStack hand = player.getItemInHand(interactionHand);
		if (hand.getItem() instanceof ILinker || hand.getItem() instanceof NodeBlockItem)
			return InteractionResult.PASS;
		BlockEntity be = level.getBlockEntity(blockPos);
		if (be instanceof IItemHolderNode node) {
			ItemStack stack = node.getHolder().getStackInSlot(0);
			if (!stack.isEmpty()) {
				if (!level.isClientSide()) {
					ItemStack extracted = node.getHolder().extractItem(0, stack.getMaxStackSize(), false);
					player.getInventory().placeItemBackInInventory(extracted);
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

}
