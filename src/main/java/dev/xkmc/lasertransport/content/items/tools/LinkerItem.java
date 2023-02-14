package dev.xkmc.lasertransport.content.items.tools;

import dev.xkmc.l2library.util.nbt.NBTObj;
import dev.xkmc.lasertransport.content.tile.base.ILinkableNode;
import dev.xkmc.lasertransport.init.data.LangData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
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

public class LinkerItem extends Item implements ILinker {

	private static final String KEY_POS = "first_pos";

	@Nullable
	public static BlockPos getPos(ItemStack stack) {
		if (stack.getTag() != null && stack.getTag().contains(KEY_POS, Tag.TAG_COMPOUND)) {
			return new NBTObj(stack, KEY_POS).toBlockPos();
		}
		return null;
	}

	public LinkerItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext ctx) {
		BlockEntity be = ctx.getLevel().getBlockEntity(ctx.getClickedPos());
		BlockEntity old = null;
		BlockPos storedPos = getPos(stack);
		if (storedPos != null) {
			old = ctx.getLevel().getBlockEntity(storedPos);
		}
		if (old instanceof ILinkableNode node) {
			if (old.getBlockPos().distSqr(ctx.getClickedPos()) > node.getMaxDistanceSqr()) {
				old = null;
			}
		}
		if (old instanceof ILinkableNode node) {
			if (node.isTargetValid(ctx.getClickedPos())) {
				if (!ctx.getLevel().isClientSide()) {
					node.link(ctx.getClickedPos());
					stack.removeTagKey(KEY_POS);
				}
				return InteractionResult.SUCCESS;
			}
		}
		if (be instanceof ILinkableNode) {
			if (!ctx.getLevel().isClientSide()) {
				new NBTObj(stack, KEY_POS).fromBlockPos(ctx.getClickedPos());
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.WAND_LINK.get());
	}

	@Override
	public boolean storesPos() {
		return true;
	}

}
