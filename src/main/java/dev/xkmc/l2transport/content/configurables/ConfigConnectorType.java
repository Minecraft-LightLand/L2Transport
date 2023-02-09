package dev.xkmc.l2transport.content.configurables;

import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.network.chat.MutableComponent;

public enum ConfigConnectorType {
	SIMPLE(LangData.SIMPLE, LangData.INFO_GATED, true),
	EXTRACT(LangData.RETRIEVE, LangData.INFO_EXTRACT, true),
	DISTRIBUTE(LangData.DISTRIBUTE, LangData.INFO_GATED, false),
	ORDERED(LangData.ORDERED, LangData.INFO_GATED, false),
	SYNC(LangData.SYNCED, LangData.INFO_SYNC, true);

	private final boolean canSetCount;
	private final LangData lang, info;

	ConfigConnectorType(LangData lang, LangData info, boolean canSetCount) {
		this.lang = lang;
		this.info = info;
		this.canSetCount = canSetCount;
	}

	public boolean canSetCount() {
		return canSetCount;
	}

	public MutableComponent getDesc() {
		return lang.get();
	}

	public LangData getInfo() {
		return info;
	}
}
