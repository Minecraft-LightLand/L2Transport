package dev.xkmc.l2transport.content.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.configurables.BaseConfigurable;
import dev.xkmc.l2transport.content.configurables.IConfigurableFilter;
import dev.xkmc.l2transport.content.client.overlay.TooltipBuilder;
import dev.xkmc.l2transport.content.client.overlay.TooltipType;
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

	private final BaseConfigurable config;

	public SyncedConnector(IntSupplier max, BaseConfigurable config) {
		super(max);
		this.config = config;
	}

	@Override
	public List<BlockPos> getVisibleConnection() {
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
	public boolean testConsumption(long avail, long c) {
		return avail == c;
	}

	@Override
	public boolean shouldContinue(long available, long consumed, long size) {
		return true;
	}

	@Override
	public long provide(long available, long consumed, long size) {
		return config.getMaxTransfer();
	}

	@Override
	public <T> void addTooltips(TooltipBuilder list, IConfigurableFilter filter) {
		if (filter.shouldDisplay()) {
			list.add(TooltipType.FILTER, LangData.INFO_FILTER.get(filter.getFilterDesc()));
			list.add(TooltipType.GATE, LangData.INFO_SYNC.getLiteral(filter.getMaxTransfer()));
		}
		list.add(TooltipType.STAT, LangData.INFO_SPEED.getLiteral(maxCoolDown.getAsInt() / 20f));
		list.add(TooltipType.DESC, LangData.SYNCED.get());
	}

}
