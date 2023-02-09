package dev.xkmc.l2transport.content.configurables;

import net.minecraft.network.chat.MutableComponent;

public interface IConfigurableFilter {

	MutableComponent getFilterDesc();

	boolean shouldDisplay();

	long getMaxTransfer();

}
