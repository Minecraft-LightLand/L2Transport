package dev.xkmc.l2transport.content.tile.item;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2transport.content.configurables.ConfigConnectorType;
import dev.xkmc.l2transport.content.configurables.ItemConfigurable;
import dev.xkmc.l2transport.content.connector.IConnector;
import dev.xkmc.l2transport.content.connector.SyncedConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class SyncedItemNodeBlockEntity extends AbstractItemNodeBlockEntity<SyncedItemNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final ItemConfigurable config = new ItemConfigurable(ConfigConnectorType.SYNC, this);

	@SerialClass.SerialField(toClient = true)
	private final SyncedConnector connector = new SyncedConnector(this::getMaxCoolDown, config);

	public SyncedItemNodeBlockEntity(BlockEntityType<SyncedItemNodeBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public IConnector getConnector() {
		return connector;
	}

	@Override
	public ItemConfigurable getConfig() {
		return config;
	}

}
