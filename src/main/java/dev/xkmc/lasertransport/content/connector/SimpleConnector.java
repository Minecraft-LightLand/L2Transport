package dev.xkmc.lasertransport.content.connector;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lasertransport.content.configurables.BaseConfigurable;
import dev.xkmc.lasertransport.util.Holder;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

@SerialClass
public class SimpleConnector extends SingleCoolDownConnector {

	@SerialClass.SerialField(toClient = true)
	private Holder pos = new Holder(null);

	protected final BaseConfigurable config;

	public SimpleConnector(IntSupplier max, BaseConfigurable config) {
		super(max);
		this.config = config;
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
	public boolean link(BlockPos pos) {
		if (this.pos.t() != null && this.pos.t().equals(pos)) {
			this.pos = new Holder(null);
			return false;
		} else {
			this.pos = new Holder(pos);
			return true;
		}
	}

	@Override
	public void removeIf(Predicate<BlockPos> o) {
		if (pos.t() == null) return;
		if (o.test(pos.t())) pos = new Holder(null);
	}

	@Override
	public long provide(long available, long consumed, long size) {
		return Math.min(config.getMaxTransfer(), super.provide(available, consumed, size));
	}

}
