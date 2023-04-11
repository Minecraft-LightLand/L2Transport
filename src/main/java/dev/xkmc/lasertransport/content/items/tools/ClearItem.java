package dev.xkmc.lasertransport.content.items.tools;

import dev.xkmc.lasertransport.content.capability.base.ITargetTraceable;
import dev.xkmc.lasertransport.content.tile.base.AbstractNodeBlockEntity;
import dev.xkmc.lasertransport.content.tile.base.ILinkableNode;
import dev.xkmc.lasertransport.init.data.LangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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
				if (!ctx.getPlayer().isShiftKeyDown()) {
					node.removeAll();
					return InteractionResult.SUCCESS;
				}
				if (be instanceof AbstractNodeBlockEntity<?> xbe) {
					var list = xbe.popUpgrade();
					if (list.size() > 0) {
						list.forEach(ctx.getPlayer().getInventory()::placeItemBackInInventory);
						xbe.markDirty();
						return InteractionResult.SUCCESS;
					}
				}
				if (be instanceof ITargetTraceable) {
					ctx.getPlayer().getInventory().placeItemBackInInventory(be.getBlockState().getBlock().asItem().getDefaultInstance());
					ctx.getLevel().setBlockAndUpdate(be.getBlockPos(), Blocks.AIR.defaultBlockState());
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
