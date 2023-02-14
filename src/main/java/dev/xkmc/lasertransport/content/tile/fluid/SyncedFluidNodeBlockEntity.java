package dev.xkmc.lasertransport.content.tile.fluid;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.lasertransport.content.configurables.ConfigConnectorType;
import dev.xkmc.lasertransport.content.configurables.FluidConfigurable;
import dev.xkmc.lasertransport.content.connector.IConnector;
import dev.xkmc.lasertransport.content.connector.SyncedConnector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class SyncedFluidNodeBlockEntity extends AbstractFluidNodeBlockEntity<SyncedFluidNodeBlockEntity> {

	@SerialClass.SerialField(toClient = true)
	private final FluidConfigurable config = new FluidConfigurable(ConfigConnectorType.SYNC, this);

	@SerialClass.SerialField(toClient = true)
	private final SyncedConnector connector = new SyncedConnector(this::getMaxCoolDown, config);

	public SyncedFluidNodeBlockEntity(BlockEntityType<SyncedFluidNodeBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public IConnector getConnector() {
		return connector;
	}

	@Override
	public FluidConfigurable getConfig() {
		return config;
	}
}
