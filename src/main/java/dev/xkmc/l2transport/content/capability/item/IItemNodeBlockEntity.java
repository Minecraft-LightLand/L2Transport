package dev.xkmc.l2transport.content.capability.item;

import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;
import net.minecraft.world.item.ItemStack;

public interface IItemNodeBlockEntity extends INodeBlockEntity {

	boolean isItemStackValid(ItemStack stack);

	int getMaxTransfer();

}
