package dev.xkmc.l2transport.content.capability.base;

import dev.xkmc.l2transport.content.connector.Connector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface INodeBlockEntity {

	Connector getConnector();

	@Nullable
	Level getLevel();

	void refreshCoolDown(BlockPos target, boolean success, boolean simulate);

	BlockPos getBlockPos();

}
