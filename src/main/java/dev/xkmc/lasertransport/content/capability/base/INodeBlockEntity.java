package dev.xkmc.lasertransport.content.capability.base;

import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.lasertransport.content.configurables.BaseConfigurable;
import dev.xkmc.lasertransport.content.connector.IConnector;
import dev.xkmc.lasertransport.content.items.upgrades.Upgrade;
import dev.xkmc.lasertransport.content.items.upgrades.UpgradeFlag;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

public interface INodeBlockEntity extends ITargetTraceable {

	IConnector getConnector();

	BaseConfigurable getConfig();

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
