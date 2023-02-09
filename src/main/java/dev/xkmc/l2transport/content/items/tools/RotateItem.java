package dev.xkmc.l2transport.content.items.tools;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

public class RotateItem extends Item implements ILinker {

	private static final Property<?>[] PROPS = {
			BlockStateProperties.FACING,
			BlockStateProperties.HORIZONTAL_FACING,
			BlockStateProperties.FACING_HOPPER,
			BlockStateProperties.AXIS,
			BlockStateProperties.HORIZONTAL_AXIS
	};

	public RotateItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext ctx) {
		BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
		for (var prop : PROPS) {
			if (state.hasProperty(prop)) {
				if (!ctx.getLevel().isClientSide()) {
					ctx.getLevel().setBlockAndUpdate(ctx.getClickedPos(), state.cycle(prop));
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public boolean storesPos() {
		return false;
	}

}
