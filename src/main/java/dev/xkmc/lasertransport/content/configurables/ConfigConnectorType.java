package dev.xkmc.lasertransport.content.configurables;

import dev.xkmc.lasertransport.init.data.LangData;
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

	public int getMinFilter() {
		return this == EXTRACT ? 0 : 1;
	}

	public MutableComponent getFilterDesc() {
		if (this == EXTRACT) return LangData.INFO_EXTRACT.get("");
		if (this == SYNC) return LangData.INFO_SYNC.get("");
		return LangData.INFO_GATED.get("");
	}

}
