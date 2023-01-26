package dev.xkmc.l2transport.content.capability.base;

import dev.xkmc.l2transport.content.capability.item.ItemNodeTarget;
import dev.xkmc.l2transport.content.flow.IContentToken;
import dev.xkmc.l2transport.content.flow.INetworkNode;
import dev.xkmc.l2transport.content.flow.INodeHolder;
import net.minecraft.world.item.ItemStack;
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

}
