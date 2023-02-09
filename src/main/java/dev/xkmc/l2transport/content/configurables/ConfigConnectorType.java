package dev.xkmc.l2transport.content.configurables;

import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.network.chat.MutableComponent;

public enum ConfigConnectorType {
	SIMPLE(LangData.SIMPLE, true),
	EXTRACT(LangData.RETRIEVE, true),
	DISTRIBUTE(LangData.DISTRIBUTE, false),
	ORDERED(LangData.ORDERED, false),
	SYNC(LangData.SYNCED, true);

	private final boolean canSetCount;
	private final LangData lang;

	ConfigConnectorType(LangData lang, boolean canSetCount) {
		this.lang = lang;
		this.canSetCount = canSetCount;
	}

	public boolean canSetCount() {
		return canSetCount;
	}

	public MutableComponent getDesc() {
		return lang.get();
	}

}
