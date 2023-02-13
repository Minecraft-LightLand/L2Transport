package dev.xkmc.l2transport.content.items.tools;

import dev.xkmc.l2transport.content.capability.generic.GenericCapabilityRegistry;
import dev.xkmc.l2transport.content.capability.generic.ICapabilityEntry;
import dev.xkmc.l2transport.content.capability.generic.IGenericNodeBlockEntity;
import dev.xkmc.l2transport.init.registrate.LTItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
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

	public static ItemStack of(ICapabilityEntry<?> entry) {
		ItemStack stack = LTItems.FLUX.asStack();
		stack.getOrCreateTag().putString(KEY, entry.id().toString());
		return stack;
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
		if (entity instanceof IGenericNodeBlockEntity node) {
			if (!level.isClientSide()) {
				node.setType(get(ctx.getItemInHand()), level.getGameTime());
			}
			return InteractionResult.SUCCESS;
		}
		return super.useOn(ctx);
	}

	@Override
	public Component getName(ItemStack stack) {
		return super.getName(stack).copy().append(" - ").append(get(stack).getTypeDesc());
	}
}
