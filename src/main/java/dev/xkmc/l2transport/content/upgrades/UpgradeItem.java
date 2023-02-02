package dev.xkmc.l2transport.content.upgrades;

import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.l2transport.content.tile.base.IUpgradableBlock;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;

public class UpgradeItem extends Item {

	private final Upgrade upgrade;

	public UpgradeItem(Properties props, Upgrade upgrade) {
		super(props);
		this.upgrade = upgrade;
	}

	public Upgrade getUpgrade() {
		return upgrade;
	}

	public UpgradeFlag getFlag() {
		return getUpgrade().getFlag();
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		BlockEntity be = ctx.getLevel().getBlockEntity(ctx.getClickedPos());
		Player player = ctx.getPlayer();
		if (player != null && be instanceof IUpgradableBlock tile) {
			GenericItemStack<UpgradeItem> gen = new GenericItemStack<>((UpgradeItem) ctx.getItemInHand().getItem(), ctx.getItemInHand().copy());
			if (!tile.acceptUpgrade(gen)) {
				return InteractionResult.FAIL;
			}
			if (ctx.getLevel().isClientSide()) {
				return InteractionResult.SUCCESS;
			}
			var opt = tile.addUpgrade(gen);
			if (opt.isPresent()) {
				ItemStack ans = opt.get();
				ctx.getItemInHand().shrink(1);
				player.getInventory().placeItemBackInInventory(ans);
			}
			return InteractionResult.SUCCESS;
		}
		return super.useOn(ctx);
	}

}
