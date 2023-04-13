package dev.xkmc.lasertransport.content.items.tools;

import dev.xkmc.l2library.serial.codec.TagCodec;
import dev.xkmc.lasertransport.content.tile.base.ILinkableNode;
import dev.xkmc.lasertransport.content.tile.extend.MultiLevelTarget;
import dev.xkmc.lasertransport.init.data.LangData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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

	public record LinkData(ResourceLocation level, BlockPos pos, boolean crossDim) {

		public static LinkData of(UseOnContext ctx, ILinkableNode node) {
			ResourceLocation level = ctx.getLevel().dimension().location();
			return new LinkData(level, ctx.getClickedPos(), node.crossDimension());
		}
	}

	public static void sendMessage(UseOnContext ctx, LangData data) {
		if (ctx.getPlayer() instanceof ServerPlayer player) {
			player.sendSystemMessage(data.get(), true);
		}
	}

	private static final String KEY = "first";

	@Nullable
	public static LinkData getData(ItemStack stack) {
		if (stack.getTag() != null && stack.getTag().contains(KEY, Tag.TAG_COMPOUND)) {
			return TagCodec.valueFromTag(stack.getOrCreateTagElement(KEY), LinkData.class);
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
		LinkData data = getData(stack);
		if (data != null) {
			if (ctx.getLevel().dimension().location().equals(data.level)) {
				old = ctx.getLevel().getBlockEntity(data.pos);
			} else if (data.crossDim()) {
				if (ctx.getLevel().isClientSide()) {
					return InteractionResult.SUCCESS;
				} else {
					Level other = new MultiLevelTarget(data.level(), data.pos()).getLevel(ctx.getLevel());
					if (other != null) {
						old = other.getBlockEntity(data.pos());
					}
				}
			}
		}
		if (old instanceof ILinkableNode node) {
			if (!node.crossDimension() && old.getBlockPos().distSqr(ctx.getClickedPos()) > node.getMaxDistanceSqr()) {
				old = null;
			}
		}
		if (old instanceof ILinkableNode node) {
			if (node.isTargetValid(ctx.getClickedPos())) {
				if (!ctx.getLevel().isClientSide()) {
					LangData result = node.link(ctx.getClickedPos(), ctx.getLevel());
					stack.removeTagKey(KEY);
					sendMessage(ctx, result);
				}
				return InteractionResult.SUCCESS;
			}
		}
		if (be instanceof ILinkableNode node) {
			if (!ctx.getLevel().isClientSide()) {
				var comp = TagCodec.valueToTag(LinkData.of(ctx, node));
				if (comp != null) {
					stack.getOrCreateTag().put(KEY, comp);
					sendMessage(ctx, LangData.MSG_LINKER_FIRST);
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.WAND_LINK.get());
		LinkData data = getData(stack);
		if (data != null) {
			list.add(LangData.INFO_ENDER_LEVEL.get(data.level().getPath()));
			list.add(LangData.INFO_ENDER_POS.get(data.pos().getX(), data.pos().getY(), data.pos().getZ()));
		}
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return getData(stack) != null;
	}

	@Override
	public boolean storesPos() {
		return true;
	}

}
