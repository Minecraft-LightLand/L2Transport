package dev.xkmc.l2transport.content.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.flow.IContentHolder;
import dev.xkmc.l2transport.content.tile.client.overlay.TooltipBuilder;
import dev.xkmc.l2transport.content.tile.client.overlay.TooltipType;
import dev.xkmc.l2transport.init.data.LangData;
import dev.xkmc.l2transport.util.Holder;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

@SerialClass
public class SimpleConnector extends SingleCoolDownConnector {

	@SerialClass.SerialField(toClient = true)
	private Holder pos = new Holder(null);

	protected final IntSupplier limit;

	public SimpleConnector(IntSupplier max, IntSupplier limit) {
		super(max);
		this.limit = limit;
	}

	@Override
	public List<BlockPos> getVisibleConnection() {
		return pos.t() == null ? List.of() : List.of(pos.t());
	}

	@Nullable
	public BlockPos getPos() {
		return pos.t();
	}

	@Override
	public void link(BlockPos pos) {
		if (this.pos.t() != null && this.pos.t().equals(pos)) {
			this.pos = new Holder(null);
		} else {
			this.pos = new Holder(pos);
		}
	}

	@Override
	public void removeIf(Predicate<BlockPos> o) {
		if (pos.t() == null) return;
		if (o.test(pos.t())) pos = new Holder(null);
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
	public long provide(long available, long consumed, long size) {
		return Math.min(super.provide(available, consumed, size), limit.getAsInt());
	}

}
