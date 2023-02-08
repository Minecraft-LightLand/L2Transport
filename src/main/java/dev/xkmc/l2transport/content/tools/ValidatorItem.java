package dev.xkmc.l2transport.content.tools;

import dev.xkmc.l2transport.content.tile.base.ILinkableNode;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ValidatorItem extends Item implements ILinker {

	public ValidatorItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext ctx) {
		BlockEntity be = ctx.getLevel().getBlockEntity(ctx.getClickedPos());
		if (be instanceof ILinkableNode node) {
			if (!ctx.getLevel().isClientSide()) {
				node.validate();
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public boolean storesPos() {
		return true;
	}

}
