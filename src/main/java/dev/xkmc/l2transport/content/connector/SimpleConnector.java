package dev.xkmc.l2transport.content.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.flow.IContentHolder;
import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

@SerialClass
public class SimpleConnector extends SingleCoolDownConnector {

	@Nullable
	@SerialClass.SerialField(toClient = true)
	public BlockPos pos = null;

	private final IntSupplier limit;

	public SimpleConnector(int max, IntSupplier limit) {
		super(max);
		this.limit = limit;
	}

	@Override
	public List<BlockPos> getConnected() {
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
	public <T> void addTooltips(List<MutableComponent> list, IContentHolder<T> filter) {
		if (filter.getCount() > 0) {
			list.add(LangData.INFO_FILTER.get(filter.getDesc()).withStyle(ChatFormatting.BLUE));
			list.add(LangData.INFO_GATED.get(filter.getCount()).withStyle(ChatFormatting.BLUE));
		}
		list.add(LangData.INFO_SPEED.get(maxCoolDown).withStyle(ChatFormatting.BLUE));
		list.add(LangData.SIMPLE.get().withStyle(ChatFormatting.GRAY));
	}

	@Override
	public int provide(int available, int consumed, int size) {
		return Math.min(super.provide(available, consumed, size), limit.getAsInt());
	}

}
