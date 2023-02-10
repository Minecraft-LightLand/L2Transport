package dev.xkmc.l2transport.content.items.tools;

import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkHooks;

public class ConfiguratorItem extends Item implements ILinker {

	public ConfiguratorItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext ctx) {
		BlockEntity be = ctx.getLevel().getBlockEntity(ctx.getClickedPos());
		if (be instanceof INodeBlockEntity node) {
			if (!ctx.getLevel().isClientSide() && ctx.getPlayer() instanceof ServerPlayer player) {
				var menu = node.getConfig().getMenu();
				if (menu != null) {
					NetworkHooks.openScreen(player, menu, be.getBlockPos());
				}
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
