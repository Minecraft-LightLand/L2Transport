package dev.xkmc.l2transport.content.items.tools;

import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.WAND_ROTATE.get());
	}

	@Override
	public boolean storesPos() {
		return false;
	}

}
