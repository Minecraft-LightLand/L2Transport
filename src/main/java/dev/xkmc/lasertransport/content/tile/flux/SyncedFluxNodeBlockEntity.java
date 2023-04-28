package dev.xkmc.lasertransport.content.tile.flux;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lasertransport.content.configurables.ConfigConnectorType;
import dev.xkmc.lasertransport.content.configurables.FluxConfigurable;
import dev.xkmc.lasertransport.content.connector.IConnector;
import dev.xkmc.lasertransport.content.connector.SyncedConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class SyncedFluxNodeBlockEntity extends AbstractFluxNodeBlockEntity<SyncedFluxNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final FluxConfigurable config = new FluxConfigurable(ConfigConnectorType.SYNC, this);

	@SerialClass.SerialField(toClient = true)
	private final SyncedConnector connector = new SyncedConnector(this::getMaxCoolDown, config);

	public SyncedFluxNodeBlockEntity(BlockEntityType<SyncedFluxNodeBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public IConnector getConnector() {
		return connector;
	}

	@Override
	public FluxConfigurable getConfig() {
		return config;
	}
}
