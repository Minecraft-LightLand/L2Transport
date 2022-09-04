package dev.xkmc.l2transport.content.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.flow.IContentHolder;
import dev.xkmc.l2transport.content.tile.base.CoolDownType;
import dev.xkmc.l2transport.content.tile.client.TooltipBuilder;
import dev.xkmc.l2transport.content.tile.client.TooltipType;
import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

@SerialClass
public class ExtractConnector extends SimpleConnector {

	private final Supplier<BlockPos> target;

	public ExtractConnector(IntSupplier max, IntSupplier limit, Supplier<BlockPos> target) {
		super(max, limit);
		this.target = target;
	}

	@Override
	public List<BlockPos> getVisibleConnection() {
		return pos == null ? List.of(target.get()) : List.of(pos, target.get());
	}

	@Override
	public List<BlockPos> getAvailableTarget() {
		return pos == null ? List.of() : List.of(pos);
	}

	@Override
	public int getCoolDown(BlockPos pos) {
		return pos.equals(target.get()) ? 0 : super.getCoolDown(pos);
	}

	@Override
	public CoolDownType getType(BlockPos pos) {
		return pos.equals(target.get()) ? CoolDownType.RETRIEVE : super.getType(pos);
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
