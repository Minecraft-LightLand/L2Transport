package dev.xkmc.l2transport.content.tile.item;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.connector.Connector;
import dev.xkmc.l2transport.content.connector.SyncedConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class SyncedItemNodeBlockEntity extends AbstractItemNodeBlockEntity<SyncedItemNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final SyncedConnector connector = new SyncedConnector(80, this::getLimit);

	public SyncedItemNodeBlockEntity(BlockEntityType<SyncedItemNodeBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public Connector getConnector() {
		return connector;
	}

	private int getLimit() {
		return getItem().isEmpty() ? 1 : getItem().getCount();
	}

}
