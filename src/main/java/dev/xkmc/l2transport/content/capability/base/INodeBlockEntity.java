package dev.xkmc.l2transport.content.capability.base;

import dev.xkmc.l2library.util.code.Wrappers;
import dev.xkmc.l2transport.content.connector.IConnector;
import dev.xkmc.l2transport.content.upgrades.Upgrade;
import dev.xkmc.l2transport.content.upgrades.UpgradeFlag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

public interface INodeBlockEntity extends ITargetTraceable {

	IConnector getConnector();

	void refreshCoolDown(BlockPos target, boolean success, boolean simulate);

	default boolean isReady() {
		return getConnector().isReady() && getThis().getLevel() != null;
	}

	@Nullable
	Upgrade getUpgrade(UpgradeFlag level);

	void markDirty();

	default INodeBlockEntity asNode() {
		return Wrappers.cast(this);
	}

}
