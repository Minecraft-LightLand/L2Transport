package dev.xkmc.lasertransport.content.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lasertransport.content.configurables.BaseConfigurable;
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
	public boolean link(BlockPos pos) {
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
		return config.getFixedTransfer();
	}

}
