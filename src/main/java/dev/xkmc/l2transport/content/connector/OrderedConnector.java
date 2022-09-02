package dev.xkmc.l2transport.content.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.flow.IContentHolder;
import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
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
	public List<BlockPos> getConnected() {
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
	public <T> void addTooltips(List<MutableComponent> list, IContentHolder<T> filter) {
		if (filter.getCount() > 0) {
			list.add(LangData.INFO_FILTER.get(filter.getDesc()).withStyle(ChatFormatting.BLUE));
		}
		list.add(LangData.INFO_SPEED.get(maxCoolDown).withStyle(ChatFormatting.BLUE));
		list.add(LangData.ORDERED.get().withStyle(ChatFormatting.GRAY));
	}

}
