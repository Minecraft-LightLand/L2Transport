package dev.xkmc.l2transport.content.connector;

import dev.xkmc.l2transport.content.flow.IContentHolder;
import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;
import java.util.function.IntSupplier;

public class ExtractConnector extends SimpleConnector {

	public ExtractConnector(int max, IntSupplier limit) {
		super(max, limit);
	}

	@Override
	public <T> void addTooltips(List<MutableComponent> list, IContentHolder<T> filter) {
		if (filter.getCount() > 0) {
			list.add(LangData.INFO_FILTER.get(filter.getDesc()).withStyle(ChatFormatting.BLUE));
			list.add(LangData.INFO_EXTRACT.get(filter.getCount()).withStyle(ChatFormatting.BLUE));
		}
		list.add(LangData.INFO_SPEED.get(maxCoolDown).withStyle(ChatFormatting.BLUE));
		list.add(LangData.RETRIEVE.get().withStyle(ChatFormatting.GRAY));
	}

}
