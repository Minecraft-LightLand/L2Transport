package dev.xkmc.l2transport.content.capability.item;

import dev.xkmc.l2transport.content.connector.Connector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface ItemNodeEntity {

	boolean isItemStackValid(ItemStack stack);

	Connector getConnector();

	@Nullable
	Level getLevel();

	void refreshCoolDown(BlockPos target, boolean success, boolean simulate);

	BlockPos getBlockPos();


}
