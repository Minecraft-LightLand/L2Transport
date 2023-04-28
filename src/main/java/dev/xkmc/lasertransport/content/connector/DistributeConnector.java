package dev.xkmc.lasertransport.content.connector;

import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

@SerialClass
public class DistributeConnector extends SingleCoolDownConnector {

	@SerialClass.SerialField(toClient = true)
	public ArrayList<BlockPos> list = new ArrayList<>();

	@SerialClass.SerialField(toClient = true)
	private int id;

	public DistributeConnector(IntSupplier max) {
		super(max);
	}

	@Override
	public List<BlockPos> getVisibleConnection() {
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
	public boolean link(BlockPos pos) {
		id = 0;
		if (list.contains(pos)) {
			list.remove(pos);
			return false;
		} else {
			list.add(pos);
			return true;
		}
	}

	@Override
	public void removeIf(Predicate<BlockPos> o) {
		list.removeIf(o);
		id = 0;
	}

	@Override
	public boolean shouldContinue(long available, long consumed, long size) {
		return consumed == 0 && super.shouldContinue(available, consumed, size);
	}

}
