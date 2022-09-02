package dev.xkmc.l2transport.content.capability.item;

import dev.xkmc.l2transport.content.capability.base.INodeBlockEntity;
import dev.xkmc.l2transport.content.connector.Connector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface IItemNodeBlockEntity extends INodeBlockEntity {

	boolean isItemStackValid(ItemStack stack);

}
