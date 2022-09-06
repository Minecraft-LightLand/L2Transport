package dev.xkmc.l2transport.content.tools;

import dev.xkmc.l2transport.content.capability.generic.GenericCapabilityRegistry;
import dev.xkmc.l2transport.content.capability.generic.ICapabilityEntry;
import dev.xkmc.l2transport.content.capability.generic.IGenericNodeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public class FluxFilter extends Item implements ILinker {

	private static final String KEY = "flux_type";

	public static ICapabilityEntry<?> get(ItemStack stack) {
		ResourceLocation id = Optional.ofNullable(stack.getTag())
				.filter(e -> e.contains(KEY, Tag.TAG_STRING))
				.map(e -> new ResourceLocation(e.getString(KEY))).orElse(null);
		return GenericCapabilityRegistry.getOrDefault(id);
	}

	public static ICapabilityEntry<?> rotate(ItemStack stack) {
		ResourceLocation id = Optional.ofNullable(stack.getTag())
				.filter(e -> e.contains(KEY, Tag.TAG_STRING))
				.map(e -> new ResourceLocation(e.getString(KEY))).orElse(null);
		id = GenericCapabilityRegistry.next(id);
		stack.getOrCreateTag().putString(KEY, id.toString());
		return GenericCapabilityRegistry.getOrDefault(id);
	}

	public FluxFilter(Properties properties) {
		super(properties);
	}

	@Override
	public boolean storesPos() {
		return false;
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level level = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		BlockEntity entity = level.getBlockEntity(pos);
		ItemStack stack = ctx.getItemInHand();
		Player player = ctx.getPlayer();
		if (entity instanceof IGenericNodeBlockEntity node) {
			if (!level.isClientSide()) {
				node.setType(get(ctx.getItemInHand()), level.getGameTime());
			}
			return InteractionResult.SUCCESS;
		}
		if (player != null && !level.isClientSide) {
			ICapabilityEntry<?> ans = rotate(stack);
			((ServerPlayer) player).sendSystemMessage(ans.getTypeDesc(), true);
		}
		return super.useOn(ctx);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide) {
			ICapabilityEntry<?> ans = rotate(stack);
			((ServerPlayer) player).sendSystemMessage(ans.getTypeDesc(), true);
		}
		return InteractionResultHolder.success(stack);
	}

}
