package dev.xkmc.l2transport.content.items.tools;

import dev.xkmc.l2transport.content.tile.base.AbstractNodeBlockEntity;
import dev.xkmc.l2transport.content.tile.base.ILinkableNode;
import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClearItem extends Item implements ILinker {

	public ClearItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext ctx) {
		BlockEntity be = ctx.getLevel().getBlockEntity(ctx.getClickedPos());
		if (be instanceof ILinkableNode node) {
			if (ctx.getPlayer() != null && !ctx.getLevel().isClientSide()) {
				if (!ctx.getPlayer().isShiftKeyDown())
					node.removeAll();
				else if (be instanceof AbstractNodeBlockEntity<?> xbe) {
					xbe.popUpgrade().forEach(ctx.getPlayer().getInventory()::placeItemBackInInventory);
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.WAND_CLEAR.get());
	}

	@Override
	public boolean storesPos() {
		return true;
	}

}
