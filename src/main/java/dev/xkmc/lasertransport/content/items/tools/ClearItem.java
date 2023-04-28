package dev.xkmc.lasertransport.content.items.tools;

import dev.xkmc.lasertransport.content.tile.base.ILinkableNode;
import dev.xkmc.lasertransport.content.tile.base.PopContentTile;
import dev.xkmc.lasertransport.content.tile.base.SpecialRetrieveTile;
import dev.xkmc.lasertransport.events.ItemConvertEvents;
import dev.xkmc.lasertransport.init.data.LangData;
import dev.xkmc.lasertransport.init.data.TagGen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClearItem extends Item implements ILinker {

	public ClearItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext ctx) {
		BlockEntity be = ctx.getLevel().getBlockEntity(ctx.getClickedPos());
		if (ctx.getPlayer() == null || ctx.getLevel().isClientSide())
			return InteractionResult.SUCCESS;
		if (be instanceof ILinkableNode node) {
			if (!ctx.getPlayer().isShiftKeyDown()) {
				node.removeAll();
				return InteractionResult.SUCCESS;
			}
		}
		if (!ctx.getPlayer().isShiftKeyDown()) {
			return InteractionResult.SUCCESS;
		}
		if (be instanceof PopContentTile xbe) {
			var list = xbe.popContents();
			if (list.size() > 0) {
				for (ItemStack content : list) {
					ItemStack next = ItemConvertEvents.convert(content, ctx.getPlayer());
					ctx.getPlayer().getInventory().placeItemBackInInventory(next);
				}
				xbe.markDirty();
				return InteractionResult.SUCCESS;
			}
		}
		BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
		if (state.is(TagGen.RETRIEVABLE)) {
			if (be instanceof SpecialRetrieveTile tile) {
				for (ItemStack back : tile.getDrops()) {
					ItemStack next = ItemConvertEvents.convert(back, ctx.getPlayer());
					ctx.getPlayer().getInventory().placeItemBackInInventory(next);
				}
				return InteractionResult.SUCCESS;
			}
			ItemStack back = state.getBlock().asItem().getDefaultInstance();
			ItemStack next = ItemConvertEvents.convert(back, ctx.getPlayer());
			ctx.getPlayer().getInventory().placeItemBackInInventory(next);
			ctx.getLevel().setBlockAndUpdate(ctx.getClickedPos(), Blocks.AIR.defaultBlockState());
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.SUCCESS;
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
