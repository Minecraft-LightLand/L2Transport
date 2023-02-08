package dev.xkmc.l2transport.content.capability.base;

import dev.xkmc.l2transport.content.capability.item.ComposterTarget;
import dev.xkmc.l2transport.content.capability.item.ItemNodeTarget;
import dev.xkmc.l2transport.content.capability.item.LevelBlockTarget;
import dev.xkmc.l2transport.content.capability.item.LevelItemTarget;
import dev.xkmc.l2transport.content.flow.ErrorNode;
import dev.xkmc.l2transport.content.flow.IContentToken;
import dev.xkmc.l2transport.content.flow.INetworkNode;
import dev.xkmc.l2transport.content.flow.INodeHolder;
import dev.xkmc.l2transport.content.upgrades.LevelDropUpgrade;
import dev.xkmc.l2transport.content.upgrades.LevelPlaceUpgrade;
import dev.xkmc.l2transport.content.upgrades.UpgradeFlag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public interface ItemStackNode extends BaseNodeHolder<ItemStack, IItemHandler> {

	@Nullable
	default INodeHolder<ItemStack> castNode(IItemHandler cap) {
		return (cap instanceof ItemStackNode node) ? node : null;
	}

	default Capability<IItemHandler> getCapability() {
		return ForgeCapabilities.ITEM_HANDLER;
	}

	default INetworkNode<ItemStack> createLeaf(BlockEntity target, IItemHandler cap, IContentToken<ItemStack> token) {
		return new ItemNodeTarget(target, cap, token);
	}

	default INetworkNode<ItemStack> getWorldNode(BlockPos pos, IContentToken<ItemStack> token) {
		Level level = entity().getThis().getLevel();
		assert level != null;
		if (level.getBlockState(pos) == Blocks.COMPOSTER.defaultBlockState()) {
			return new ComposterTarget(level, pos, token);
		}
		var upgrade = entity().getUpgrade(UpgradeFlag.LEVEL);
		if (upgrade instanceof LevelDropUpgrade) {
			return new LevelItemTarget(level, pos, token);
		}
		if (upgrade instanceof LevelPlaceUpgrade) {
			return new LevelBlockTarget(level, pos, token);
		}
		return new ErrorNode<>(pos);
	}

}
