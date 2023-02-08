package dev.xkmc.l2transport.content.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.flow.IContentHolder;
import dev.xkmc.l2transport.content.tile.client.overlay.TooltipBuilder;
import dev.xkmc.l2transport.content.tile.client.overlay.TooltipType;
import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

@SerialClass
public class OrderedConnector extends SingleCoolDownConnector {

	@SerialClass.SerialField(toClient = true)
	public TreeSet<BlockPos> set = new TreeSet<>(this::comparator);

	private final BlockEntity center;

	public OrderedConnector(BlockEntity center, IntSupplier max) {
		super(max);
		this.center = center;
	}

	@Override
	public List<BlockPos> getVisibleConnection() {
		return new ArrayList<>(set);
	}

	@Override
	public void link(BlockPos pos) {
		if (set.contains(pos)) set.remove(pos);
		else set.add(pos);
	}

	@Override
	public void removeIf(Predicate<BlockPos> o) {
		set.removeIf(o);
	}

	private int comparator(BlockPos a, BlockPos b) {
		return Double.compare(center.getBlockPos().distSqr(a), center.getBlockPos().distSqr(b));
	}

	@Override
	public <T> void addTooltips(TooltipBuilder list, IContentHolder<T> filter) {
		if (filter.getCount() > 0) {
			list.add(TooltipType.FILTER, LangData.INFO_FILTER.get(filter.getDesc()));
		}
		list.add(TooltipType.STAT, LangData.INFO_SPEED.getLiteral(maxCoolDown.getAsInt() / 20f));
		list.add(TooltipType.DESC, LangData.ORDERED.get());
	}

}
