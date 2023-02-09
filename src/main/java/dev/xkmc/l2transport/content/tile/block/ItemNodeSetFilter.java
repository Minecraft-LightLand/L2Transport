package dev.xkmc.l2transport.content.tile.block;

import dev.xkmc.l2library.block.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2library.block.mult.DefaultStateBlockMethod;
import dev.xkmc.l2library.block.mult.OnClickBlockMethod;
import dev.xkmc.l2transport.content.items.tools.ILinker;
import dev.xkmc.l2transport.content.items.upgrades.UpgradeItem;
import dev.xkmc.l2transport.content.tile.item.AbstractItemNodeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public class ItemNodeSetFilter implements OnClickBlockMethod, CreateBlockStateBlockMethod, DefaultStateBlockMethod {

	public static final ItemNodeSetFilter ITEM = new ItemNodeSetFilter();

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player pl, InteractionHand hand, BlockHitResult result) {
		ItemStack stack = pl.getMainHandItem();
		if (stack.getItem() instanceof ILinker)
			return InteractionResult.PASS;
		if (stack.getItem() instanceof UpgradeItem)
			return InteractionResult.PASS;
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		BlockEntity te = level.getBlockEntity(pos);
		if (te instanceof AbstractItemNodeBlockEntity<?> rte) {
			if (rte.getConfig().hasNoFilter()) {
				if (!stack.isEmpty()) {
					rte.getConfig().setSimpleFilter(stack.copy());
					level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, true));
					return InteractionResult.SUCCESS;
				}
			}
			if (!stack.isEmpty() && rte.getConfig().canQuickSetCount(stack)) {
				rte.getConfig().setMaxTransfer(stack.getCount());
				rte.markDirty();
				return InteractionResult.SUCCESS;
			}
			rte.getConfig().clearFilter();
			level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, false));
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.LIT);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(BlockStateProperties.LIT, false);
	}

}