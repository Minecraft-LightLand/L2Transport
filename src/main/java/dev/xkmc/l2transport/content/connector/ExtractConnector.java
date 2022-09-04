package dev.xkmc.l2transport.content.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.flow.IContentHolder;
import dev.xkmc.l2transport.content.tile.client.TooltipBuilder;
import dev.xkmc.l2transport.content.tile.client.TooltipType;
import dev.xkmc.l2transport.init.data.LangData;

import java.util.function.IntSupplier;

@SerialClass
public class ExtractConnector extends SimpleConnector {

	public ExtractConnector(IntSupplier max, IntSupplier limit) {
		super(max, limit);
	}

	@Override
	public <T> void addTooltips(TooltipBuilder list, IContentHolder<T> filter) {
		if (filter.getCount() > 0) {
			list.add(TooltipType.FILTER, LangData.INFO_FILTER.get(filter.getDesc()));
			list.add(TooltipType.GATE, LangData.INFO_EXTRACT.getLiteral(filter.getCount()));
		} else {
			list.add(TooltipType.GATE, LangData.INFO_EXTRACT.getLiteral(limit.getAsInt()));
		}
		list.add(TooltipType.STAT, LangData.INFO_SPEED.getLiteral(maxCoolDown.getAsInt() / 20f));
		list.add(TooltipType.DESC, LangData.RETRIEVE.get());
	}

}
