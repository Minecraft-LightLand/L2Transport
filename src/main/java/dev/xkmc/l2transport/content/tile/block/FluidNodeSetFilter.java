package dev.xkmc.l2transport.content.tile.block;

import dev.xkmc.l2library.block.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2library.block.mult.DefaultStateBlockMethod;
import dev.xkmc.l2library.block.mult.OnClickBlockMethod;
import dev.xkmc.l2transport.content.items.tools.ILinker;
import dev.xkmc.l2transport.content.items.upgrades.UpgradeItem;
import dev.xkmc.l2transport.content.tile.fluid.AbstractFluidNodeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;

public class FluidNodeSetFilter implements OnClickBlockMethod, CreateBlockStateBlockMethod, DefaultStateBlockMethod {

	public static final FluidNodeSetFilter FLUID = new FluidNodeSetFilter();

	@Override
	public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player pl, InteractionHand hand, BlockHitResult result) {
		if (pl.getMainHandItem().getItem() instanceof ILinker)
			return InteractionResult.PASS;
		if (pl.getMainHandItem().getItem() instanceof UpgradeItem)
			return InteractionResult.PASS;
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		BlockEntity te = level.getBlockEntity(pos);
		if (te instanceof AbstractFluidNodeBlockEntity<?> rte) {
			var stackCap = pl.getMainHandItem().getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
			if (stackCap.resolve().isPresent()) {
				FluidStack stack = stackCap.resolve().get().getFluidInTank(0);
				if (rte.getConfig().hasNoFilter()) {
					rte.getConfig().setSimpleFilter(stack);
					level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, true));
					return InteractionResult.SUCCESS;
				}
				if (rte.getConfig().canQuickSetCount(stack)) {
					rte.getConfig().setMaxTransfer(stack.getAmount());
					rte.markDirty();
					return InteractionResult.SUCCESS;
				}
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