package dev.xkmc.l2transport.content.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.flow.IContentHolder;
import dev.xkmc.l2transport.content.tile.client.TooltipBuilder;
import dev.xkmc.l2transport.content.tile.client.TooltipType;
import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

@SerialClass
public class SyncedConnector extends SingleCoolDownConnector {

	@SerialClass.SerialField(toClient = true)
	public ArrayList<BlockPos> list = new ArrayList<>();

	private final IntSupplier limit;

	public SyncedConnector(IntSupplier max, IntSupplier limit) {
		super(max);
		this.limit = limit;
	}

	@Override
	public List<BlockPos> getConnected() {
		return list;
	}

	@Override
	public void link(BlockPos pos) {
		if (list.contains(pos)) list.remove(pos);
		else list.add(pos);
	}

	@Override
	public void removeIf(Predicate<BlockPos> o) {
		list.removeIf(o);
	}

	@Override
	public boolean testConsumption(int avail, int c) {
		return avail == c;
	}

	@Override
	public boolean shouldContinue(int available, int consumed, int size) {
		return true;
	}

	@Override
	public int provide(int available, int consumed, int size) {
		return limit.getAsInt();
	}

	@Override
	public <T> void addTooltips(TooltipBuilder list, IContentHolder<T> filter) {
		if (filter.getCount() > 0) {
			list.add(TooltipType.FILTER, LangData.INFO_FILTER.get(filter.getDesc()));
			list.add(TooltipType.GATE, LangData.INFO_SYNC.getLiteral(filter.getCount()));
		}
		list.add(TooltipType.STAT, LangData.INFO_SPEED.getLiteral(maxCoolDown.getAsInt() / 20f));
		list.add(TooltipType.DESC, LangData.SYNCED.get());
	}

}
