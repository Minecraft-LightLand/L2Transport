package dev.xkmc.l2transport.content.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.flow.IContentHolder;
import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;
import java.util.function.IntSupplier;

@SerialClass
public class ExtractConnector extends SimpleConnector {

	public ExtractConnector(IntSupplier max, IntSupplier limit) {
		super(max, limit);
	}

	@Override
	public <T> void addTooltips(List<MutableComponent> list, IContentHolder<T> filter) {
		if (filter.getCount() > 0) {
			list.add(LangData.INFO_FILTER.get(filter.getDesc()));
			list.add(LangData.INFO_EXTRACT.getLiteral(filter.getCount()));
		}
		list.add(LangData.INFO_SPEED.getLiteral(maxCoolDown.getAsInt() / 20f));
		list.add(LangData.RETRIEVE.get());
	}

}
