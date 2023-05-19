package dev.xkmc.lasertransport.content.connector;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.lasertransport.content.configurables.BaseConfigurable;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

@SerialClass
public class SimpleConnector extends SingleCoolDownConnector {

	@SerialClass.SerialField(toClient = true)
	private BlockPos pos = null;

	protected final BaseConfigurable config;

	public SimpleConnector(IntSupplier max, BaseConfigurable config) {
		super(max);
		this.config = config;
	}

	@Override
	public List<BlockPos> getVisibleConnection() {
		return pos == null ? List.of() : List.of(pos);
	}

	@Nullable
	public BlockPos getPos() {
		return pos;
	}

	@Override
	public boolean link(BlockPos pos) {
		if (this.pos != null && this.pos.equals(pos)) {
			this.pos = null;
			return false;
		} else {
			this.pos = pos;
			return true;
		}
	}

	@Override
	public void removeIf(Predicate<BlockPos> o) {
		if (pos == null) return;
		if (o.test(pos)) pos = null;
	}

	@Override
	public long provide(long available, long consumed, long size) {
		return Math.min(config.getMaxTransfer(), super.provide(available, consumed, size));
	}

}
