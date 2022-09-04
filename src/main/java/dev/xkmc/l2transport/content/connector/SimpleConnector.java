package dev.xkmc.l2transport.content.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.flow.IContentHolder;
import dev.xkmc.l2transport.content.tile.client.TooltipBuilder;
import dev.xkmc.l2transport.content.tile.client.TooltipType;
import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

@SerialClass
public class SimpleConnector extends SingleCoolDownConnector {

	@Nullable
	@SerialClass.SerialField(toClient = true)
	public BlockPos pos = null;

	protected final IntSupplier limit;

	public SimpleConnector(IntSupplier max, IntSupplier limit) {
		super(max);
		this.limit = limit;
	}

	@Override
	public List<BlockPos> getVisibleConnection() {
		return pos == null ? List.of() : List.of(pos);
	}

	@Override
	public void link(BlockPos pos) {
		if (this.pos != null && this.pos.equals(pos)) {
			this.pos = null;
		} else {
			this.pos = pos;
		}
	}

	@Override
	public void removeIf(Predicate<BlockPos> o) {
		if (pos == null) return;
		if (o.test(pos)) pos = null;
	}

	@Override
	public <T> void addTooltips(TooltipBuilder list, IContentHolder<T> filter) {
		if (filter.getCount() > 0) {
			list.add(TooltipType.FILTER, LangData.INFO_FILTER.get(filter.getDesc()));
			list.add(TooltipType.GATE, LangData.INFO_GATED.getLiteral(filter.getCount()));
		}
		list.add(TooltipType.STAT, LangData.INFO_SPEED.getLiteral(maxCoolDown.getAsInt() / 20f));
		list.add(TooltipType.DESC, LangData.SIMPLE.get());
	}

	@Override
	public int provide(int available, int consumed, int size) {
		return Math.min(super.provide(available, consumed, size), limit.getAsInt());
	}

}
