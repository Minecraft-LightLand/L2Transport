package dev.xkmc.l2transport.content.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.flow.IContentHolder;
import dev.xkmc.l2transport.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@SerialClass
public class DistributeConnector extends SingleCoolDownConnector {

	@SerialClass.SerialField(toClient = true)
	public ArrayList<BlockPos> list = new ArrayList<>();

	@SerialClass.SerialField(toClient = true)
	private int id;

	public DistributeConnector(int max) {
		super(max);
	}

	@Override
	public List<BlockPos> getConnected() {
		return list;
	}

	@Override
	public List<BlockPos> getAvailableTarget() {
		if (list.isEmpty()) {
			return List.of();
		}
		id %= list.size();
		List<BlockPos> ans = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			ans.add(list.get((id + i) % list.size()));
		}
		return ans;
	}

	@Override
	public void perform() {
		super.perform();
		id++;
	}

	@Override
	public void link(BlockPos pos) {
		if (list.contains(pos)) list.remove(pos);
		else list.add(pos);
		id = 0;
	}

	@Override
	public void removeIf(Predicate<BlockPos> o) {
		list.removeIf(o);
		id = 0;
	}

	@Override
	public boolean shouldContinue(int available, int consumed, int size) {
		return consumed == 0 && super.shouldContinue(available, consumed, size);
	}

	@Override
	public <T> void addTooltips(List<MutableComponent> list, IContentHolder<T> filter) {
		if (filter.getCount() > 0) {
			list.add(LangData.INFO_FILTER.get(filter.getDesc()).withStyle(ChatFormatting.BLUE));
		}
		list.add(LangData.INFO_SPEED.get(maxCoolDown).withStyle(ChatFormatting.BLUE));
		list.add(LangData.DISTRIBUTE.get().withStyle(ChatFormatting.GRAY));
	}

}
